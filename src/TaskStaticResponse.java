import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 
 * @author Juan Gutierrez Aguado (Dep. Informatica, Univ. Valencia, Spain)
 * 
 */
class TaskStaticResponse implements Runnable {
	private Socket canal;
	private PrintWriter pw;
	private OutputStream out;
	private InputStream in;
	private String path;
	private String priv;
	private String accessFile;

	/**
	 * Constructor
	 * 
	 * @param c
	 *            the socket channel
	 * @param path
	 *            the path where resources are located
	 * @param priv
	 *            the prefix of the directories that require authentication
	 * @param accessFile
	 *            the name of the files that stores the user;password
	 */
	TaskStaticResponse(Socket c, String path, String priv, String accessFile) {
		try {
			canal = c;
			in = canal.getInputStream();
			out = canal.getOutputStream();
			pw = new PrintWriter(out);
			this.path = path;
			this.priv = priv;
			this.accessFile = accessFile;
		} catch (Exception ex) {
		}

	}

	public void run() {

		// This HTTP server responds to GET requests

		String request = null;
		try {
			System.out
					.println("---------------------------------- NEW REQUEST ------------------------");
			System.out.println(new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss")
					.format(Calendar.getInstance().getTime()));

			request = UtilsHTTP.readLine(in);
			System.out.println(request);

			String httpMethod = UtilsHTTP.getMethod(request);
			HashMap<String, String> headers = null;

			headers = UtilsHTTP.getHeaders(in);

			// If there is a request method in the request
			if (httpMethod != null) {
				String recurso = UtilsHTTP.getResource(request);

				// It is forbidden to ask the password file
				if (UtilsHTTP.isPassFile(request, accessFile))
					UtilsHTTP.writeForbidden(pw);

				// If the resource requires authentication
				else if ((httpMethod.equals("GET"))
						&& (UtilsHTTP.requiresAuthentication(request, priv))) {

					String auth = headers.get("Authorization");

					if (auth == null)
						// If there is no Authorization header
						// Write the response asking for user/pass
						UtilsHTTP.writeResponseRequiresAuthorization(pw);
					else {
						// There is Authorization header

						// Get the user/pass part of that header
						String[] upb = auth.trim().split(" ");
						auth = upb[1];

						// Decode Base64
						String user = UtilsHTTP.getUser(auth);
						String pass = UtilsHTTP.getPass(auth);

						// Read the file with user;pass and parse it
						String authFile = UtilsHTTP.getPathToPassFile(recurso,
								path, accessFile);
						BufferedReader br = new BufferedReader(new FileReader(
								authFile));
						String l = br.readLine();
						String[] up = l.split(";");

						br.close();

						if ((up[0].equals(user)) && (up[1].equals(pass)))
							// If the user/password is correct return the
							// resource
							UtilsHTTP.writeResource(pw, out, path + recurso);
						else
							// Otherwise write the response asking for user/pass
							UtilsHTTP.writeResponseRequiresAuthorization(pw);

					}
				} else if (httpMethod.equals("GET"))
					UtilsHTTP.writeResource(pw, out, path + recurso);
				else if ((httpMethod.equals("DELETE"))
						|| (httpMethod.equals("PUT")))
					UtilsHTTP.writeResponseNotImplemented(pw, httpMethod);

			}

		} catch (Exception e) {
			// In case of error write a Internal Server Error (500) response
			UtilsHTTP.writeResponseServerError(pw);
		} finally {
			try {
				in.close();
				out.close();
				pw.close();
				canal.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}