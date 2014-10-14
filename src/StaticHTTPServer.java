import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This is not a production static HTTP server.
 * It is intended for TEACHING purposes.
 *
 * Use at your own risk!
 */

/**
 * This is the class with the main method Juan Gutierrez Aguado (Dep.
 * Informatica, Univ. Valencia, Spain)
 */
class StaticHTTPServer {
	private static String path;
	private static String priv;
	private static String credsFile;
	private static int nThreads;
	private static int port;

	/**
	 * This method reads properties from a configuration file.
	 * If the file does not exists then default values are used.
	 * The values are stored in the fields of this class
	 */
	private static void readProperties() {
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("config.ini"));
			path = p.getProperty("PATH", "/var/web/");
			priv = p.getProperty("AUTH_PREFIX", "private");
			credsFile = p.getProperty("CREDENTIALS", ".creds");
			nThreads = Integer.parseInt(p.getProperty("NUM_THREADS", "50"));
			port = Integer.parseInt(p.getProperty("SERVER_PORT", "8080"));

			p = null;
		} catch (Exception ex) {
			System.out
					.println("You can provide a config.ini file to configure the server.");
			System.out.println("PATH=path");
			System.out.println("AUTH_PREFIX=prefix");
			System.out.println("CREDENTIALS=file");
			System.out
					.println("NUM_THREADS=maximum numer of concurrent threads");
			System.out.println("SERVER_PORT=port");
		}

		System.out
				.println("-----  THIS SERVER WAS DESIGNED FOR TEACHING PURPOSES ----------");
		System.out
				.println("-----  IT CANNOT BE USED IN PRODUCTION ENVIRONMENTS   ------------");
		System.out
		        .println("-----  Juan Gutierrez Aguado                          ------------");
		System.out
				.println("------ Computer Science Dept - Univ. Valencia (Spain) ----------");
		System.out.println();

		System.out.println("Server configuration values are:");
		System.out.println("Resource location: " + path);
		System.out
				.println("Resources that require user and password are located"
						+ " in a directory that starts with : " + priv);
		System.out.println("File name with user;pass : " + credsFile);
		System.out.println("Maximum number of concurrent requests: "
				+ nThreads);
		System.out.println("Default server port :" + port);
		System.out.println();

	}

	public static void main(String[] args) {
		readProperties();
		try {

			ServerSocket s = new ServerSocket(port);

			ExecutorService executors = Executors.newFixedThreadPool(nThreads);

			while (true) {
				try {
					Socket canal = s.accept();
					Runnable handleRequest = new TaskStaticResponse(canal, path, priv,
							credsFile);
					executors.execute(handleRequest);
				} catch (Exception e) {
					System.out.println("Error dealing with client:");
					e.printStackTrace();
				}
			}

		} catch (Exception ex) {
			System.out.println("Unnable to start the HTTP server");
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
