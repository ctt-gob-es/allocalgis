/**
 * AppContext.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app;

import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.acl.AclNotFoundException;
import java.security.acl.Permission;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.administrador.init.Constantes;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.utilidades.PasswordManager;
import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.protocol.control.ISesion;
import com.geopista.protocol.control.Sesion;
import com.geopista.security.GeopistaAcl;
import com.geopista.security.IConnection;
import com.geopista.security.ISecurityManager;
import com.geopista.security.SecurityManager;
import com.geopista.security.sso.SSOAuthManager;
import com.geopista.ui.dialogs.LoginDialog;
import com.geopista.ui.dialogs.MunicipioDialog;
import com.geopista.util.ApplicationContext;
import com.geopista.util.I18NUtils;
import com.geopista.util.SecurityManagerProxy;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.GUIUtil;

import edu.emory.mathcs.backport.java.util.Collections;
import com.geopista.protocol.administrador.EncriptarPassword;

/**
 * 
 * 
 * Información compartida en el transcurso de una ejecución de la aplicación.
 * Este contexto de aplicación lo utilizan las aplicaciones para comunicarse con
 * sus partes. Debe incluirse este contexto de aplicación en el contexto del
 * editor para que los plugins tengan acceso a los servicios de acceso a
 * Geopista. TODO: Asegurarse que el componente Editor puede incorporar el
 * contexto de aplicación dentro del blackboard de su workbenchcontexto.
 * 
 * @author juacas
 **/
public class AppContext implements ApplicationContext, IConnection {

	private static HttpClient localClient;

	private boolean isCanceled = false;
	
	private static String forceLocalgisDatabaseURL=null;
	
	/**
	 * Logger for this class
	 */
	// private static final Log logger = LogFactory.getLog(AppContext.class);
	private static final Log logger;
	static {
		createDir();
		logger = LogFactory.getLog(AppContext.class);
	}

	public static boolean hearbeat = true;

	public static boolean heartBeatInicializado = false;

	static public void setHeartBeat(boolean heartbeatValue) {
		hearbeat = heartbeatValue;
	}

	static public ApplicationContext getApplicationContext() {
		return getApplicationContext(true);
	}

	static public ApplicationContext getApplicationContext(boolean initHeartBeat) {
		if (_app == null) {
			_app = new AppContext(initHeartBeat);
		}
		return _app;
	}

	static public void setApplicationContext(ApplicationContext app) {
		_app = app;
	}

	static {
		UserPreferenceStore.setUserPreference(
				UserPreferenceConstants.LOCALGIS_DATABASE_URL, null);
	}

	// static private ApplicationContext _app = new AppContext();
	static private ApplicationContext _app = null; // new AppContext();

	private ResourceBundle rBConfiguration; // configuracion
	private ResourceBundle rbI18n;// internacionalizacion
	private ResourceBundle rBCustomConfiguration;// configuracion de usuario

	/**
	 * Contenedor de propiedades. Mapa String->Object utilizado para intercambio
	 * de información
	 */
	private static Blackboard _blackboard = new Blackboard();
	private JFrame _parentFrame = null;

	/**
	 * Guardo el identificador de la entidad y el municipio asociados al usuario
	 */

	/**
	 * Referencia al componente editor para reutilizarlo TODO: Revisar el
	 * comportamiento de un mismo componente insertado en diversos containers.
	 */

	private boolean offlineMode = false;
	private ISecurityManager secMgr = null;

	public AppContext() {
		this(true);
	}

	/**
     *  
     */
	public AppContext(boolean inicializarHeartBeat) {

		rBConfiguration = initializeProperties("GeoPista"); // Carga el fichero
															// de configuración
		String lenguage = UserPreferenceStore.getUserPreference(
				UserPreferenceConstants.DEFAULT_LOCALE_KEY, "es_ES", true);
		String leng = lenguage.substring(0, 2);
		String pais = lenguage.substring(3, 5);
		defLocale = new Locale(leng, pais);
		Locale.setDefault(defLocale);
		loadI18NResource("GeoPistai18n");
		secMgr = new SecurityManagerProxy();
		secMgr.setsUrl(getString(UserPreferenceConstants.LOCALGIS_SERVER_PRINCIPAL_URL));
		if (hearbeat && inicializarHeartBeat)
			initHeartBeat();

	}

	public static void createDir() {
		File file = new File("logs");

		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
     * 
     */
	public void initHeartBeat() {
		if (SecurityManager.getObjetoTestConnection() != null)
			return;

		// Podemos inhabilitar que se inicie el Heartbeat en algunos casos por
		// ejemplo
		// se han detectado casos en que desde el propio servidor

		if (!SecurityManager.isInitHeartBeat())
			return;

		// Si el heartbeat ya ha sido lanzado evitamos arrancar
		// el hilo. Nos ponemos como listeners simplemente
		if (heartBeatInicializado) {
			if (hearbeat){
				SecurityManager.getTestConnection().setIconnection(this);
				return;
			}
			return;
		}
		heartBeatInicializado = true;

		if (hearbeat){
			SecurityManager.setHeartBeatTime(Integer.parseInt(getString(
					UserPreferenceConstants.LOCALGIS_SERVER_HEARTBEAT, "100000")));
			SecurityManager.getTestConnection().setIconnection(this);
		}

		if (hearbeat)
			SecurityManager.callHeartBeat();

	}

	/**
	 * Paramos el Heartbeat
	 */
	public void stopHeartBeat() {

		SecurityManager.getTestConnection().finish();

	}

	public void setUrl(String url) {
		Constantes.url = url;
		SecurityManager.setsUrl(url);
	}

	/**
	 * Obtiene el recurso de configuración por defecto del sistema
	 * 
	 * @param string
	 * @return
	 */
	private ResourceBundle initializeProperties(String string) {
		return ResourceBundle.getBundle(string);
	}

	/**
	 * Busca en el directorio de usuario el fichero de configuración. Si no
	 * existe copia el que se suministra con el JAR.
	 * 
	 * Si no se puede copiar utiliza el disponible en el JAR
	 * 
	 * @deprecated
	 * @param string
	 *            con el nombre el fichero de configuración
	 * @return
	 */
	private ResourceBundle initializePropertiesOld(String string) {
		if (logger.isDebugEnabled()) {
			logger.debug("initializeProperties(String) - start");
		}
		String userDirPath = System.getProperty("user.home");

		File userDir = new File(userDirPath);
		File propFile = new File(userDir, "GeoPista.properties");

		if (!propFile.exists()) {
			logger.info("initializeProperties(String) - Fichero inexistente. Copiando fichero de propiedades. : String userDirPath = "
					+ userDirPath);
			Properties props = new Properties();
			try {
				props.load(AppContext.class
						.getResourceAsStream("/GeoPista.properties"));
				props.store(new FileOutputStream(propFile),
						"GeoPista Local Config");
			} catch (IOException e1) {

				ResourceBundle returnResourceBundle = ResourceBundle
						.getBundle("GeoPista");
				logger.warn(
						"initializeProperties(String) - No se puede realizar la copia local de la configuración. Usando configuración por defecto. : File propFile = "
								+ propFile, e1);

				return returnResourceBundle;
			}

		}
		try {
			FileInputStream stream = new FileInputStream(propFile);
			ResourceBundle rb = new PropertyResourceBundle(stream);

			logger.info("initializeProperties(String) - Cargando configuración. : File propFile = "
					+ propFile);

			return rb;
		} catch (IOException e) {
			logger.error(
					"initializeProperties(String) - No se puede cargar el fichero de configuración. : File propFile = "
							+ propFile, e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("initializeProperties(String) - end");
		}

		ResourceBundle returnResourceBundle = ResourceBundle
				.getBundle("GeoPista");
		if (logger.isDebugEnabled()) {
			logger.debug("initializeProperties(String) - Cargado el recurso por defecto.");
		}

		return returnResourceBundle;
	}

	private Vector listeners = new Vector();
	private boolean partialLogged;

	/**
	 * REgistra listeners para los eventos relativos al contexto de aplicacion
	 * 
	 * @param listener
	 * @see GeopistaEvent
	 * @see AppContextListener
	 */
	public void addAppContextListener(AppContextListener listener) {
		listeners.add(listener);
	}

	public void removeAppContextListener(AppContextListener listener) {
		listeners.remove(listener);
	}

	private void fireEvent(GeopistaEvent ev) {
		Enumeration listener = listeners.elements();
		AppContextListener toCall = null;
		while (listener.hasMoreElements()) {
			toCall = (AppContextListener) listener.nextElement();
			if (toCall != null)
				toCall.connectionStateChanged(ev);
		}
	}

	public boolean isLogged() {
		initHeartBeat();

		return SecurityManager.isLogged();
	}

	public boolean isOnlyLogged() {

		return SecurityManager.isOnlyLogged();
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public boolean isPartialLogged() {
		return partialLogged;
	}

	public void setPartialLogged(boolean partialLogged) {
		this.partialLogged = partialLogged;
	}

	public boolean isOnline() {
		initHeartBeat();
		if (isOfflineMode())
			return false;
		return SecurityManager.isConnected();
	}

	/**
	 * Pasa las credenciales del usuario
	 * 
	 */
	public void login() {
		if (isOfflineMode())
			return;

		// Si el usuario ha cancelado no le volvemos a preguntar por la
		// autenticación.
		if (isCanceled())
			return;

		LoginDialog dial;
		boolean resultSSO = SSOAuthManager.ssoAuthManager(defaultProfile);
		if (isOnlyLogged() || resultSSO) {

			if (seleccionarEntidad(getMainFrame())) {
				fireEvent(new GeopistaEvent(this, GeopistaEvent.LOGGED_IN));
				showMunicipiosEntidad();
			} else {
				logger.info("Seleccion de entidad cancelada");
				return;
				// fireEvent(new GeopistaEvent(this, GeopistaEvent.LOGGED_OUT));
				// UserPreferenceStore.setUserPreference(USER_LOGIN, null);
			}
		} else if (!isLogged()) {
			/*
			 * String CodigoEntidad =
			 * getUserPreference("geopista.DefaultEntityId","0",false);
			 * if(CodigoEntidad==null||CodigoEntidad.trim().equals("0")) {
			 * JOptionPane
			 * .showMessageDialog(getMainFrame(),getI18nString("idEntidadNoValido"
			 * )); return; }
			 */
			// dial = new LoginDialog(getMainFrame(),defaultProfile);
			dial = new LoginDialog(getMainFrame(), defaultProfile, false);
			dial.setTitle(getMessage("CAuthDialog.title"));
			if (getMainFrame() == null)
				GUIUtil.centreOnScreen(dial);
			else
				GUIUtil.centreOnWindow(dial);

			PasswordManager passwordManager = new PasswordManager();

			while (true) {
				// Comprobamos el numero de intentos
				passwordManager.comprobarIntentos(dial.getLogin());
				// dial.setPassword("");
				dial.show();
				if (dial.isAutenticated()) {
					isCanceled = false;
					String nombreUsuario=dial.getLogin();
					Constantes.nombreUsuario=nombreUsuario;
	                Constantes.contrasenna=dial.getPassword();
					seleccionarEntidad(getMainFrame());
					fireEvent(new GeopistaEvent(this, GeopistaEvent.LOGGED_IN));
					// UserPreferenceStore.setUserPreference(USER_LOGIN,
					// dial.getLogin());
					showMunicipiosEntidad();

					break;
				} else if (!dial.isCanceled()) {
					isCanceled = false;
					try {
						SecurityManager.login(dial.getLogin(),
								dial.getPassword(), defaultProfile);
						
						String nombreUsuario=dial.getLogin();
						Constantes.nombreUsuario=nombreUsuario;
		                Constantes.contrasenna=dial.getPassword();
						passwordManager.seguridadContrasenia(dial.getLogin());

						// Verificamos el resultado final e informamos del
						// numero de reintentos
						// restantes.
						if (passwordManager.verificacionContraseña(
								defaultProfile, true)) {
							SSOAuthManager.saveSSORegistry();
							// SecurityManager.login("SYSSUPERUSER",
							// "SYSPASSWORD","Geopista", 1);
							if (!seleccionarEntidad(getMainFrame())) {
								logger.info("Selección de entidad cancelada");
								// fireEvent(new GeopistaEvent(this,
								// GeopistaEvent.LOGGED_OUT));
								// UserPreferenceStore.setUserPreference(USER_LOGIN,
								// null);
							} else {
								fireEvent(new GeopistaEvent(this,
										GeopistaEvent.LOGGED_IN));
								UserPreferenceStore
										.setUserPreference(
												UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,
												dial.getLogin());
								showMunicipiosEntidad();
							}
							break;
						} else {
							if (!SSOAuthManager.isSSOActive())
								logout();
							SecurityManager.unLogged();
						}
					} catch (Exception e) {
						logger.error("login()", e);
						dial.setErrorLabel(_app
								.getI18nString("AppContext.IntentoErroneo"));
						UserPreferenceStore.setUserPreference(
								UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,
								null);

						// Verificamos el resultado final e informamos del
						// numero de reintentos
						// restantes.
						passwordManager.verificacionContraseña(defaultProfile,
								false);
					}
				} else {
					isCanceled = true;
					fireEvent(new GeopistaEvent(this, GeopistaEvent.LOGGED_OUT));
					UserPreferenceStore.setUserPreference(
							UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,
							null);
					break;
				}
			}

		}
	}

	public void login2(String nombreUsuario, String profile) {
		PasswordManager passwordManager = new PasswordManager();
		try {

			passwordManager.seguridadContrasenia(nombreUsuario);

			// Verificamos el resultado final e informamos del numero de
			// reintentos
			// restantes.
			if (passwordManager.verificacionContraseña(profile, true)) {
				if (SSOAuthManager.isSSOActive())
					SSOAuthManager.saveSSORegistry();
			} else if (SSOAuthManager.isSSOActive()) {
				SecurityManager.logout();
				AppContext.getApplicationContext().logout();
			}
		} catch (Exception e) {
			logger.error("login()", e);
			UserPreferenceStore.setUserPreference(
					UserPreferenceConstants.DEFAULT_LOGIN_USERNAME, null);

			// Verificamos el resultado final e informamos del numero de
			// reintentos
			// restantes.
			passwordManager.verificacionContraseña(defaultProfile, false);
		}
	}

	public void logout()
    {
        try
        {
        	if (SecurityManager.isLogged())
        		SecurityManager.logout();
        } catch (Exception e) //TODO: revise extra-wide exception handle
        {
            logger.error("logout()", e);
        }
        fireEvent(new GeopistaEvent(this, GeopistaEvent.LOGGED_OUT));
        UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME, null);
    }
    
    /**
     * Devuelve una conexión compartida a la base de datos.
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException
    {
    	
    	return getConnection(true);
    }
    
    /**
     * Devuelve una conexión compartida a la base de datos.
     * @return
     * @throws SQLException
     */
    public Connection getConnection(boolean forceLogin) throws SQLException
    {
        //Lo lógico es que se hiciera login para autentificarse en el sistema.
        //Hemos comentado la linea porque no correctamente el login()
        //if (!isLogged()) login();
        try
        {
        	if (forceLogin){
	            if (!isLogged())
	            {
	                login();
	            }
        	}
            
        	String url=null;
        	if (forceLocalgisDatabaseURL!=null)
        		url=forceLocalgisDatabaseURL;
        	else
        		url = getString(UserPreferenceConstants.LOCALGIS_DATABASE_URL);
        	
            String driverClass = rBConfiguration.getString(UserPreferenceConstants.LOCALGIS_DATABASE_DRIVER);
            
            //System.out.println("Driver Class"+driverClass);
            Enumeration enDrivers = DriverManager.getDrivers();
            while(enDrivers.hasMoreElements()){
            	DriverManager.deregisterDriver((java.sql.Driver)enDrivers.nextElement());
            }
            java.sql.Driver driver=(java.sql.Driver)Class.forName(driverClass).newInstance();

            //System.out.println("Driver:"+driver);
            DriverManager.registerDriver(driver);
            //System.out.println("URL conexion:"+url);
            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        } catch (ClassNotFoundException e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        } catch (Exception e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        }
    }
    
 
    /**
     * Devuelve una conexión compartida a la base de datos.
     * @return
     * @throws SQLException
     */
    public Connection getJDBCConnection() throws SQLException
    {       
        try
        {
            if (!isLogged())
            {
                login();
            }            
            
            String url = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL);
            String driverClass = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER);
            String user = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_USER);
            String pass =getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS);
            //String pass =UserPreferenceStore.getUserPreference(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS,"",false);
            Enumeration enDrivers = DriverManager.getDrivers();
            while(enDrivers.hasMoreElements()){
            	DriverManager.deregisterDriver((java.sql.Driver)enDrivers.nextElement());
            }
            
            java.sql.Driver driver=(java.sql.Driver)Class.forName(driverClass).newInstance();

            DriverManager.registerDriver(driver);
            Connection conn = DriverManager.getConnection(url, user, pass);
            
            return conn;
        } catch (SQLException e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        } catch (ClassNotFoundException e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        } catch (Exception e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        }
    }
    
    /**
     * Devuelve una conexión compartida a la base de datos.
     * @return
     * @throws SQLException
     */
    public Connection getJDBCConnection(String user, String pass) throws SQLException
    {       
        try
        {
            if (!isLogged())
            {
                login();
            }            
            
            String url = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_URL);
            String driverClass = AppContext.getApplicationContext().getString(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_DRIVER);
            Enumeration enDrivers = DriverManager.getDrivers();
            while(enDrivers.hasMoreElements()){
            	DriverManager.deregisterDriver((java.sql.Driver)enDrivers.nextElement());
            }
           
            java.sql.Driver driver=(java.sql.Driver)Class.forName(driverClass).newInstance();

            DriverManager.registerDriver(driver);
            Connection conn = DriverManager.getConnection(url, user, pass);
            
            return conn;
        } catch (SQLException e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        } catch (ClassNotFoundException e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        } catch (Exception e)
        {
            logger.error("getConnection()", e);
            throw new SQLException("ConnectionError");
        }
    }
    
    
    /**
     * Devuelve el Frame de la aplicacion para referencia de dialogos y otros.
     * @return Returns the _parentFrame.
     */
    public JFrame getMainFrame()
    {
        return _parentFrame;
    }
    /**
     * @param frame The _parentFrame to set.
     */
    public void setMainFrame(JFrame frame)
    {
        _parentFrame = frame;
    }
    /**
     * @return Returns the editor.
     */
    public Object getEditor(String id)
    {
        return (Object) _blackboard.get("editor." + id);
    }
    /**
     * @param editor The editor to set.
     */
    public void setEditor(String id, Object editor)
    {
        _blackboard.put("editor." + id, editor);
    }
    /**
     * @param editor The editor to remove.
         	@baseSatec Destruccion del elemento.
     */
     
    public void removeEditor(String id)
    {
        _blackboard.remove("editor." + id);
    }  
   
  
    /**
     * 
     * @param id Identificación del mapa guardado
     * @return
     */
    public Object getMap(String id)
    {
        return  _blackboard.get("model." + id);
    }
    /**
     * Guarda una referencia al mapa para reutilizarlo entre componentes y ahorrar tiempo de carga
     * @param id Identificación del mapa guardado
     * @param map The map to set.
     */
    public void storeMap(String id, Object map)
    {
        _blackboard.put("model." + id, map);
    }
    public void clearMap(String id)
    {
    	Object map = getMap(id);
        storeMap(id, null);// clears entry
        System.gc(); //Try to free up memory
    }
/*    public Object getSharedEditor(String id)
    {
        Task map = getMap(id);
        if (map==null)
            return new GeopistaEditor();
        else
            return new GeopistaEditor((ITask) map);
    }*/
    /**
     * Método estático para facilitar la internacionalizacion con los IDE
     * Obtiene las traducciones del Bundle GeoPista
     * @param id Nombre del fichero de recursos
     * @return
     */
    public static String getMessage(String id)
    {
        return getApplicationContext().getI18nString(id);
    }
    
    /**
     * Traducción del término para el Locale y recurso actual
     * @see com.geopista.util.ApplicationContext#getI18nString(java.lang.String)
     */
    public String getI18nString(String resourceId)
    {
        if (resourceId==null) return null;
        String traduc = I18NUtils.i18n_getname(resourceId, rbI18n);
        if (traduc.equals(resourceId))
        {
        	if (rBCustomConfiguration!=null)
        		traduc= I18NUtils.i18n_getname(resourceId, rBCustomConfiguration);
        }
        
        return traduc;
    }
    
    public Blackboard getBlackboard()
    {
        return _blackboard;
    }
    
    /* (non-Javadoc)
     * @see com.geopista.util.ApplicationContext#setProfile(java.lang.String)
     */
    private String defaultProfile = "Geopista";
    private GeopistaAcl defaultAcl;
    
    /**
     * Cambia el locale por defecto del contexto.
     * Se inicializa automaticamente a partir de la clave 
     * geopista.user.locale
     * @param locale
     */
    public void setLocale(Locale locale)
    {
        Locale.setDefault(locale);
    }
    
    public void setProfile(String idProfile) throws AclNotFoundException
    {
        if (!idProfile.equals(defaultProfile))
        {
            defaultProfile = idProfile;
            try
            {
                defaultAcl = SecurityManager.getPerfil(defaultProfile);
                
            } catch (Exception e)
            {
                // En caso de que ocurra una excepcion inesperada anular referencias
                // TODO: Revisar si es necesario un uso tan general de Exception
                defaultAcl = null;
                logger.error("setProfile(String)", e);
            }
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.geopista.util.ApplicationContext#checkPermission(com.geopista.security.GeopistaPermission, java.lang.String)
     */
    
    public boolean checkPermission(Permission permission,
            String idProfile)
    {
        try
        {
            if (!isLogged()) login();
            
            if (!isLogged())
            	return false;
            GeopistaAcl profileAcl = null;
            if (idProfile != null && !idProfile.equals(defaultProfile)) profileAcl = SecurityManager
            .getPerfil(idProfile);
            
            return profileAcl.checkPermission(SecurityManager.getPrincipal(),
                    permission);
            
        } catch (AclNotFoundException e)
        {
            
            logger.error("checkPermission(GeopistaPermission, String)", e);
        } catch (Exception e)
        {
            
            //TODO: revisar el uso de Exception
            logger.error("checkPermission(GeopistaPermission, String)", e);
        }
        return false;
    }
    
    public void closeConnection(Connection conn, PreparedStatement ps,
            Statement st, ResultSet rs)
    {
        try{
            if (rs != null)
                rs.close();
            
            if (st != null)
                st.close();
            
            
            if (ps != null)
                ps.close();
            
            if (conn != null)
                conn.close();
        } catch (Exception e)
        {
            logger
            .error(
                    "closeConnection(Connection, PreparedStatement, Statement, ResultSet)",
                    e);
        }
        
    }
    /* (non-Javadoc)
     * @see com.geopista.security.IConnection#disconnect()
     */
    public void disconnect()
    {
        fireEvent(new GeopistaEvent(this, GeopistaEvent.DESCONNECTED));
        
        //Si el administrador de cartografia se para, marcamos la conexion
        //como inactiva con el objetivo de no tener que volver a rearrancar
        //la aplicacion (Editor, Contaminantes, etc)..
        //-----NUEVO----->
    	//if(!SSOAuthManager.isSSOActive()) logout();
        //---FIN NUEVO--->
        //SecurityManager.unLogged();
        
        //2013-09-23
        //No desconectamos la sesion porque no sabemos si ha sido una perdida momentanea
        //o si si realmente se ha caido el servidor de cartografia.
        
    }
    /* (non-Javadoc)
     * @see com.geopista.security.IConnection#connect()
     */
    public void connect()
    {
        fireEvent(new GeopistaEvent(this, GeopistaEvent.RECONNECTED));
        
    }
    /**
     * Obtiene un acceso al servidor de cartografia
     * @return
     */
/*    public IAdministradorCartografiaClient getClient()
    {
        String sUrlPrefix = getString(GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA);
        return new AdministradorCartografiaClient(sUrlPrefix);
    }*/
    /**
     * Version de un solo parámetro que devuelve null por defecto.
     * Obtiene la propiedad de configuracion para la aplicacion actual
     * Busca primero en las preferencias del usuario local.
     * Despues busca en la configuración general.
     * Realiza la sustitución de patrones fijados:
     *
     * @param host_ip_key2
     * @param string
     * @return
     * @see AppContext.substitutions
     * @see AppContext.getString(String,String)
     */
    public String getString(String paramId)
    {
        return getString(paramId, null);
    }
    
    private Hashtable substitutionMap=new SubsTable();
    private Locale defLocale;
    
    class SubsTable extends Hashtable
    {
        /**
         * Logger for this class
         */
        private final Log logger = LogFactory.getLog(SubsTable.class);
        
        /**
         * Place all substitutions here
         */
        SubsTable() 
        {
            super();
            put("%HOST_IP%", UserPreferenceConstants.LOCALGIS_SERVER_HOST);
            put("%RUTA_BASE%", UserPreferenceConstants.PREFERENCES_DATA_PATH_KEY);
            put("%DDBB_SERVER_IP%",UserPreferenceConstants.DDBB_SERVER_IP);
            put("%URL_TOMCAT%", UserPreferenceConstants.TOMCAT_URL);
            put("%HOST_ADMCAR%", UserPreferenceConstants.LOCALGIS_SERVER_URL);
        }
    }
    /**
     * Realiza la sustitución de variables concretas:
     * - %HOST_IP% por el valor de geopista.conexion.serverIP
     * - %RUTA_BASE% por el valor de ruta.base.mapas
     * -etc
     * 
     * 
     * @param value expresion en la que hacer la sustitucion
     * @return expresion con las sustituciones
     */
    public String substitutions(String value, Map substitutionMap)
    {
        if (value==null) return null;
        Iterator subst=substitutionMap.entrySet().iterator();
        while (subst.hasNext()) // Make all substitutions
        {
           try{

                Map.Entry element = (Map.Entry) subst.next();
                String newValue=UserPreferenceStore.getUserPreference((String) element.getValue(),
                    rBConfiguration.getString((String) element.getValue()),
                    false);
                if (value!=null)
                    value=value.replaceAll((String) element.getKey(), newValue);
            }catch(Exception e)
            {
                logger.error("Error al hacer la subsitucion:",e);
            }
        }

        return value;
    }
    
    
    private String subtitutionsPath(String value)
    {
        
        File finalPath = null;
        if (value==null) return null;
        
        
        String basePathValue=UserPreferenceStore.getUserPreference("ruta.base.mapas",
                rBConfiguration.getString("ruta.base.mapas"),
                false);
        
        value=value.replaceAll("%RUTA_BASE%", "");
        finalPath = new File(basePathValue,value);	
        
        
        return finalPath!=null?finalPath.getAbsolutePath():null;
    }
    
    /**
     * Obtiene la propiedad de configuracion para la aplicacion actual
     * Busca primero en las preferencias del usuario local.
     * Despues busca en la configuración general.
     * Realiza la sustitución de patrones fijados:
     *
     * @param host_ip_key2
     * @param string
     * @return
     * @see AppContext.substitutions
     */
    public String getString(String paramId, String defaultString)
    {
        String value = null;
        try
        {
            paramId = paramId.trim();// limpia la clave
            value = UserPreferenceStore.getUserPreference(paramId, null,false);
            
            try {
				if (paramId.equals(UserPreferenceConstants.LOCALGIS_DATABASE_DIRECTCONNECTION_PASS)){
					EncriptarPassword enc= new EncriptarPassword(value);
					value=enc.desencriptar();
				}
				else if (paramId.equals(UserPreferenceConstants.MANTIS_PASSWORD)){
					EncriptarPassword enc= new EncriptarPassword(value);
					value=enc.desencriptar();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            if (rBCustomConfiguration!=null && value == null)
            {   
                try
                {
                    value = rBCustomConfiguration.getString(paramId);
                }
                catch(MissingResourceException mre)
                {
                    value = null;
                }
                
            }
            if (value == null)
                value = rBConfiguration.getString(paramId);
            if (value == null)
                value = defaultString;
            return substitutions(value,substitutionMap);
        } catch (RuntimeException e)
        {
            // Si hay algún problema notifica en consola y deja el término sin traducir
            if (logger.isDebugEnabled())
            {
                
                logger.debug("getString(String) - TODO: Configurar: " + paramId);
            }
            return defaultString;
        }
    }
    /**
     * Obtiene direcciones o URLs haciendo sustituciones de comodines
     */
    public String getPath(String paramId)
    {
        return getPath(paramId, null);
    }
    
    public String getPath(String paramId, String defaultString)
    {
        String value = null;
        try
        {
            paramId = paramId.replaceAll(" ", "");// limpia la clave
            value = UserPreferenceStore.getUserPreference(paramId, null,false);
            if (rBCustomConfiguration!=null && value == null)
                value = rBCustomConfiguration.getString(paramId);
            if (value == null)
                value = rBConfiguration.getString(paramId);
            if (value == null)
                value = defaultString;
            return subtitutionsPath(value);
        } catch (RuntimeException e)
        {
            // Si hay algún problema notifica en consola y deja el término sin traducir
            if (logger.isDebugEnabled())
            {
                
                logger.debug("getString(String) - TODO: Configurar: " + paramId);
            }
            return defaultString;
        }
    }
    
//    /**
//     * Fija una preferencia local de este usuario
//     * Usa un registro local para configuraciones
//     * @param key
//     * @param value
//     */
//    public void setUserPreference(String key, String value)
//    {
//        Preferences pref = Preferences.userRoot().node(UserPreferenceConstants.LOCALGIS_SYSTEM_RESGISTRY_PATH);
//        if(value==null) 
//        	pref.remove(key);
//		else{
//			try {
//				if(pref.nodeExists(key)){
//					pref.remove(key);
//				}
//			} catch (BackingStoreException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        	pref.put(key, value);
//		}
//    }
//    /**
//     * Obtiene la preferencia del usuario. Si no se ha fijado antes se extrae
//     * de la configuración de sistema y se fija para futuras consultas.
//     * 
//     * @param key
//     * @param defaultValue
//     * @param tryToCreate indica si hay que copiar la propiedad de configuraciones generales
//     * @return
//     */
//    public String getUserPreference(String key, String defaultValue, boolean tryToCreate)
//    {
//    	if (key.equals("localgis.DefaultCityId"))
//    		return String.valueOf(getIdMunicipio());
//        Preferences pref = Preferences.userRoot().node(UserPreferenceConstants.LOCALGIS_SYSTEM_RESGISTRY_PATH);
//        String value = pref.get(key, null);
//        if (value == null && tryToCreate) // Intenta extraerlo de la configuración por defecto
//        {
//            try
//            {
//                if (rBCustomConfiguration!=null)
//                {   
//                    try
//                    {
//                        value = rBCustomConfiguration.getString(key);
//                    }
//                    catch(MissingResourceException e)
//                    {
//                        value = null;
//                    }                    
//                }                
//                if (value == null)
//                    value = rBConfiguration.getString(key);
//                if (value != null) //Almacena para futuras consultas
//                    setUserPreference(key, value);
//            } catch (MissingResourceException e)
//            {
//                setUserPreference(key,defaultValue);
//                if (logger.isWarnEnabled())
//                {
//                    logger
//                    .warn(
//                            "getUserPreference(key = "
//                            + key
//                            + ", defaultValue = "
//                            + defaultValue
//                            + ") - Clave no encontrada en configuración. Fijado valor por defecto.");
//                }
//                return defaultValue;
//            }
//        }
//        //Compruebo si la dirección del administrador de cartografía tiene formato maquina en vez de
//        //protocolo://maquina:puerto
//        if (key.equals(UserPreferenceConstants.LOCALGIS_SERVER_URL))
//        	if (value!=null && !value.startsWith("http"))
//        		value = "http://"+value+":8081";;
//        
//        return value == null ? defaultValue : value;
//    }
    protected void setRbI18n(ResourceBundle rbI18n)
    {
        this.rbI18n = rbI18n;
    }
    public static void main(String[] args)
    {
        //Test appcontext
        
        logger.info("main() - Propiedad obtenida"+ AppContext.getApplicationContext().getString("geopista.conexion.servidor"));
    }
    public boolean isOfflineMode()
    {
        return offlineMode;
    }
    public void setOfflineMode(boolean offlineMode)
    {
        this.offlineMode = offlineMode;	
        if (offlineMode)
            initHeartBeat();
        else
            SecurityManager.setHeartBeatTime(Integer.MAX_VALUE);
        
    }
    /* (non-Javadoc)
     * @see com.geopista.util.ApplicationContext#setMainWindow(java.awt.Window)
     */
    Window win=null;
	private static boolean municipioSeleccionado;
    public void setMainWindow(Window windowAncestor)
    {
        win=windowAncestor;
    }
    public Window getMainWindow()
    {
        if (win==null && getMainFrame()!=null)
        {
            return SwingUtilities.getWindowAncestor(getMainFrame());
        }
        return win;
    }
    /* (non-Javadoc)
     * @see com.geopista.util.ApplicationContext#getRB()
     */
    public ResourceBundle getRB()
    {
        return rbI18n;
    }
    
    public void setMunicipioSeleccionado(boolean municipioSeleccionado){
    	this.municipioSeleccionado=municipioSeleccionado;
    }
    
    public boolean isMunicipioSeleccionado(){
    	return municipioSeleccionado;
    }
    
    public static int getIdMunicipio()
    {
    	int idMunicipio = -1;
    	ISesion iSesion = (ISesion)_blackboard.get(UserPreferenceConstants.SESION_KEY);
    	if (iSesion != null && iSesion.getIdMunicipio()!=null)
    		idMunicipio = Integer.parseInt(iSesion.getIdMunicipio());
    	return idMunicipio;
    }
    
    public static int getIdCurrentMunicipio()
    {
    	int idMunicipio = -1;
    	ISesion iSesion = (ISesion)_blackboard.get(UserPreferenceConstants.SESION_KEY);
    	if (iSesion != null && iSesion.getIdMunicipio()!=null)
    		idMunicipio = Integer.parseInt(iSesion.getIdCurrentMunicipio());
    	return idMunicipio;
    }
    
    public static int getIdEntidad()
    {
    	int idEntidad = -1;
    	ISesion iSesion = (ISesion)_blackboard.get(UserPreferenceConstants.SESION_KEY);
    	if (iSesion != null)
    		idEntidad = Integer.parseInt(iSesion.getIdEntidad());
    	return idEntidad;
    }
    
    public static int getIdCurrentEntidad()
    {
    	int idEntidad = -1;
    	ISesion iSesion = (ISesion)_blackboard.get(UserPreferenceConstants.SESION_KEY);
    	if (iSesion != null)
    		idEntidad = Integer.parseInt(iSesion.getIdCurrentEntidad());
    	return idEntidad;
    }
    
    public static void setIdMunicipio(int idMunicipio)
    {
    	try{
	    	ISesion iSesion = (ISesion)_blackboard.get(UserPreferenceConstants.SESION_KEY);
	    	if (iSesion == null)
	    		iSesion = new Sesion();
	        iSesion.setIdMunicipio(String.valueOf(idMunicipio));
	        _blackboard.put(UserPreferenceConstants.SESION_KEY, iSesion);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public static void setIdEntidad(int idEntidad)
    {
    	ISesion iSesion = (ISesion)_blackboard.get(UserPreferenceConstants.SESION_KEY);
    	if (iSesion == null)
    		iSesion = new Sesion();
        iSesion.setIdEntidad(String.valueOf(idEntidad));
        _blackboard.put(UserPreferenceConstants.SESION_KEY, iSesion);
    }

    
    public static List getAlMunicipios()
    {
    	List lista = new ArrayList();
    	ISesion iSesion = (ISesion)_blackboard.get(UserPreferenceConstants.SESION_KEY);
    	if (iSesion != null)
    		lista = iSesion.getAlMunicipios();
    	
    	//*****************************
        //Ordenacion de los municipios
        //*****************************
    	if (lista!=null){
	        Collections.sort(lista, new java.util.Comparator(){    		 
	            public int compare(Object o1, Object o2) {
	            	com.geopista.app.catastro.model.beans.Municipio p1 = (com.geopista.app.catastro.model.beans.Municipio) o1;
	            	com.geopista.app.catastro.model.beans.Municipio p2 = (com.geopista.app.catastro.model.beans.Municipio) o2;
	               return p1.getNombreOficial().compareToIgnoreCase(p2.getNombreOficial());
	            }
	 
	        });
    	}
    	
    	return lista;
    }
    
    public static void setAlMunicipios(List lista)
    {
    	ISesion iSesion = (ISesion)_blackboard.get(UserPreferenceConstants.SESION_KEY);
    	if (iSesion == null)
    		iSesion = new Sesion();
        iSesion.setAlMunicipios(lista);
    }

    /* (non-Javadoc)
     * @see com.geopista.util.ApplicationContext#loadResource(java.lang.String)
     */
    public void loadI18NResource(String resource)
    {
        rbI18n = ResourceBundle.getBundle(resource,  defLocale);// internacionalizacion
    }
    /**
     * @return Returns the rBCustomConfiguration.
     */
    public ResourceBundle getRBCustomConfiguration()
    {
        return rBCustomConfiguration;
    }
    /**
     * @param customConfiguration The rBCustomConfiguration to set.
     */
    public void setRBCustomConfiguration(ResourceBundle customConfiguration)
    {
        rBCustomConfiguration = customConfiguration;
        
    }
    /**
     * @param customConfiguration The name of the rBCustomConfiguration to set.
     */
    public void setRBCustomConfiguration(String strCustomConfiguration)
    {
        rBCustomConfiguration = initializeProperties(strCustomConfiguration+"i18n");
        
    }

    public void loadI18NResource(String resource, Locale locale){
        defLocale= locale;
        Locale.setDefault(defLocale);
        rbI18n = ResourceBundle.getBundle(resource,  defLocale);// internacionalizacion
    }

    public ResourceBundle getI18NResource(){
        return rbI18n;
    }
    public String getDefaultProfile(){
    	return defaultProfile;
    }

    public static HttpClient getHttpClient() {
    	return getHttpClient(null);
    }
    
    //Recuperamos un cliente de conexion contra el servidor. Si el SecurityManager
    //no es nulo generamos un HttpClient nuevo.
    public static HttpClient getHttpClient(SecurityManager sm) {
    	
    	//logger.info("Obteniendo manejador de conexion http para la conexion");
		if ((localClient==null) || (sm!=null))
		{
			//System.out.println("Creando httpclient....");
//			MultiThreadedHttpConnectionManager httpMgr=new MultiThreadedHttpConnectionManager();
//			httpMgr.setMaxConnectionsPerHost(1);
			
			MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
			HttpClient client = new HttpClient(connectionManager);

			client.getHttpConnectionManager().getParams().setMaxConnectionsPerHost(new HostConfiguration(),3);
			client.getHttpConnectionManager().getParams().setMaxTotalConnections(3);

			
			Credentials creds = null;
			boolean usuarioAutenticado=true;
			if (sm!=null){
            	if (sm.getIdSesionNS()==null)
            		usuarioAutenticado=false;
        	}
        	else if (SecurityManager.getIdSesion() == null){
        		usuarioAutenticado=false;
        	}
        		
        	if (!usuarioAutenticado){			
				logger.debug("Usuario no autenticado");
                creds = new UsernamePasswordCredentials("GUEST", "");
              }
            else{
            	if (sm!=null){
            		creds = new UsernamePasswordCredentials(sm.getIdSesionNS(), sm.getIdSesionNS());
            	}
            	else
            		creds = new UsernamePasswordCredentials(SecurityManager.getIdSesion(), SecurityManager.getIdSesion());
            }

    		//establish a connection within 5 seconds
    		//client.setConnectionTimeout(5000);
    		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

    		//set the default credentials
    		if (creds != null) {
    			//client.getState().setCredentials(null, null, creds);
    			AuthScope auth=new AuthScope(null,-1, null);
    			client.getState().setCredentials(auth, creds);
    		}
    		if (sm!=null){
    			return client;
    		}
    		else{
    			localClient=client;
    		}
        	
		}
		//Si hemos terminado la sesion previamente
		else if ((localClient != null) && (sm == null)) {
			//logger.info("Eliminando el estado de conexion (sesion) del usuario");
			localClient.setState(new HttpState());
		}
		return localClient;
	}
    
   
    public static void releaseResources(){
    	localClient = null;
    	
    }
    
    /**
     * Meto los municipios de la entidad asociada al usuario en el plugin de municipios
     */
    public static void showMunicipiosEntidad(){
    	
    	List list=getAlMunicipios();
    	if (list!=null){
	    	Iterator itMuni = getAlMunicipios().iterator();
	    	JComboBox jc = (JComboBox)_blackboard.get(UserPreferenceConstants.MUNI_COMBO);
	    	if (jc != null){
	    		try {
					jc.removeAllItems();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	while(itMuni.hasNext()){
		    		Municipio municipio = (Municipio)itMuni.next();
			        String nombreMunicipio = municipio.getId()+"-"+municipio.getNombreOficial();
			        nombreMunicipio = StringUtil.limitLength(nombreMunicipio, 25);
			        jc.addItem(nombreMunicipio);
		    	}
	    	}   	
    	}
    }

	public static boolean seleccionarMunicipio(Frame f){
		return seleccionarMunicipio(f,true,true);
	}
	
	/**
	 * El usuario debe seleccionar un municipio sobre el que se realizarán las operaciones
	 */
	public static boolean seleccionarMunicipio(Frame f,boolean solicitarMunicipio,boolean comprobarUsuarioYaAutenticado){
		return seleccionarMunicipio(f,solicitarMunicipio,comprobarUsuarioYaAutenticado,false);
	}
    /**
	 * El usuario debe seleccionar un municipio sobre el que se realizarán las operaciones
	 */
	public static boolean seleccionarMunicipio(Frame f,boolean solicitarMunicipio,boolean comprobarUsuarioYaAutenticado,boolean transparent){
		try{
			//Si el usuario pertenece a una entidad devuelve false y la aplicacion se cierra
			if (comprobarUsuarioYaAutenticado)
				if (AppContext.getIdMunicipio() != -1)
					return true;
			
			if (!seleccionarEntidad(f, false,transparent))
				return false;
						
			MunicipioDialog panelMunicipio=null;
			String sMunicipioSeleccionado=null;
			if (!municipioSeleccionado){
				List listaMunicipios=AppContext.getAlMunicipios();
				if (listaMunicipios.size()>0){
					if (listaMunicipios.size()>1){
						if (!solicitarMunicipio){
							sMunicipioSeleccionado=String.valueOf(((Municipio)listaMunicipios.get(0)).getId());
						}
						else{
							panelMunicipio = new MunicipioDialog(f,AppContext.getAlMunicipios());
							GUIUtil.centreOnWindow(panelMunicipio);
							panelMunicipio.setVisible(true);
						}
					}
					else
						sMunicipioSeleccionado=String.valueOf(((Municipio)listaMunicipios.get(0)).getId());
				}
			}
			if (panelMunicipio!=null){
				if (panelMunicipio.isCanceled()){
					logger.error("Seleccion de municipio cancelada");
					SecurityManager.setIdMunicipio(null);
					return false;
				}
				else{
					municipioSeleccionado=true;
					String sMunicipio = "";
					while (sMunicipio.equals("")){
						if(panelMunicipio!=null)
							sMunicipio = panelMunicipio.getMunicipio();
						else
							sMunicipio=sMunicipioSeleccionado;
						if (!sMunicipio.equals("")){
							AppContext.setIdMunicipio(Integer.parseInt(sMunicipio));
							SecurityManager.setIdMunicipio(sMunicipio);
						}else{
							JOptionPane.showMessageDialog(f,_app.getI18nString("catastro.general.error.municipio"));
						}
					}		
				}
			}
			else{
				String sMunicipio = "";
				municipioSeleccionado=true;
				if (sMunicipioSeleccionado!=null){
					sMunicipio=sMunicipioSeleccionado;
					if (!sMunicipio.equals("")){
						AppContext.setIdMunicipio(Integer.parseInt(sMunicipio));
						SecurityManager.setIdMunicipio(sMunicipio);
					}else{
						JOptionPane.showMessageDialog(f,_app.getI18nString("catastro.general.error.municipio"));
					}
				}
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(f,e.getMessage());
			return false;
		}
		return true;
	}

	
	public static boolean seleccionarEntidad(Frame f){
		return seleccionarEntidad(f,true);
	}
	
	/**
	 * El usuario debe seleccionar una entidad sobre la cual se realizarán las operaciones
	 */
	public static boolean seleccionarEntidad(Frame f,boolean comprobarUsuarioYaAutenticado){
		return seleccionarEntidad(f,comprobarUsuarioYaAutenticado,false);
	}
	
	
	/**
	 * El usuario debe seleccionar una entidad sobre la cual se realizarán las operaciones
	 */
	public static boolean seleccionarEntidad(Frame f,boolean comprobarUsuarioYaAutenticado,boolean transparent){
		try{
			
			if (comprobarUsuarioYaAutenticado)
				if (AppContext.getIdMunicipio()!=-1)
					return false;
			
			//La entidad es 0 si el usuario que se autentica es un superadministrador
			if (_blackboard.get("IDENTIDAD_ORIGINAL")==null)
				_blackboard.put("IDENTIDAD_ORIGINAL", AppContext.getIdEntidad());
			if (AppContext.getIdEntidad()==0){
				
				boolean solicitarPanelEntidad=true;
				if (transparent){
					if (AppContext.getIdCurrentEntidad()!=-1){
						logger.info("Estableciendo el identificador de entidad en el servidor por desconexion. Caso1->"+AppContext.getIdCurrentEntidad());
						SecurityManager.seleccionarEntidad(SecurityManager.getPrincipal().getName(), (String)_blackboard.get("PASS_BD"), SecurityManager.getIdApp(), String.valueOf(AppContext.getIdCurrentEntidad()));
						solicitarPanelEntidad=false;
					}
				}
				
				if (solicitarPanelEntidad){
					MunicipioDialog panelMunicipio = new MunicipioDialog(f,(List)_blackboard.get(UserPreferenceConstants.ENTIDADES),true);			
			        GUIUtil.centreOnScreen(panelMunicipio);
					panelMunicipio.setVisible(true);
					if (!panelMunicipio.isCanceled()){
						String sEntidad = panelMunicipio.getMunicipio();
						AppContext.setIdEntidad(Integer.parseInt(sEntidad));
						SecurityManager.seleccionarEntidad(SecurityManager.getPrincipal().getName(), (String)_blackboard.get("PASS_BD"), SecurityManager.getIdApp(), sEntidad);
					}
					else{		
						SecurityManager.setIdEntidad(null);
						SecurityManager.setCanceled(true);
						return false;
					}
				}
			}
			else{
				//Si se pide transparent es que se ha perdido la conexión y es necesario volver a establecer el
				//identificador de entidad para la sesion en el servidor (SeleccionarEntidad.java)
				SecurityManager.setIdEntidad(String.valueOf(AppContext.getIdEntidad()));
				if (transparent){
					logger.info("Estableciendo el identificador de entidad en el servidor por desconexion. Caso2.->"+AppContext.getIdEntidad());
					SecurityManager.seleccionarEntidad(SecurityManager.getPrincipal().getName(), (String)_blackboard.get("PASS_BD"), SecurityManager.getIdApp(), String.valueOf(AppContext.getIdEntidad()));
				}
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(f,e.getMessage());
			return false;
		}
		return true;
	}
    /**
     * Pasa las credenciales del usuario 
     *
     */
    public boolean loginNoGrafico(String user, String pass)
    {
        if (isOfflineMode())return true;
        if (!isLogged())
        {
            String CodigoMunicipio = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID,"0",false);
            if(CodigoMunicipio==null||CodigoMunicipio.trim().equals("0"))
            {                
                System.out.println(_app.getI18nString("idMunicipioNoValido"));
                return false;
            }
                    try
                    {
                        int idMunicipio = Integer.parseInt(CodigoMunicipio);                                                                                           
                        SecurityManager.login(user, pass,defaultProfile); //idMunicipio);
                        fireEvent(new GeopistaEvent(this, GeopistaEvent.LOGGED_IN));
                        UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME, user);
                    } catch (Exception e)
                    {
                        logger.error("login()", e);
                        System.out.println(_app.getI18nString("AppContext.IntentoErroneo"));
                        UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME, null);
                        return false;
                    }
        }
        return true;
    }
    
    public void relogin (String user, String pass){
    	if (isOfflineMode())return;
    	
    	String CodigoMunicipio = UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_MUNICIPALITY_ID,"0",false);
        
        if(CodigoMunicipio==null||CodigoMunicipio.trim().equals("0"))
        {                
            System.out.println(_app.getI18nString("idMunicipioNoValido"));
            return;
        }
                
                try
                {
                    int idMunicipio = Integer.parseInt(CodigoMunicipio);                                                                                           
                    SecurityManager.relogin();
                    fireEvent(new GeopistaEvent(this, GeopistaEvent.LOGGED_IN));
                    UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME, user);
                } catch (Exception e)
                {
                    logger.error("login()", e);
                    System.out.println(_app.getI18nString("AppContext.IntentoErroneo"));
                    UserPreferenceStore.setUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME, null);
                }
    }	
	
    public GeometryFactory getGeometryFactory(){
    	PrecisionModel precisionModel = new PrecisionModel(100000000);
    	GeometryFactory geometryFactory = new GeometryFactory(precisionModel);
    	return geometryFactory;
    }
    
    public GeometryFactory getGeometryFactory(int precision){
    	PrecisionModel precisionModel = new PrecisionModel(precision);
    	GeometryFactory geometryFactory = new GeometryFactory(precisionModel);
    	return geometryFactory;
    }

	@Override
	public String getDecrytedPassword()  throws Exception {
		// TODO Auto-generated method stub
		
		String password = getString(UserPreferenceConstants.LOCALGIS_LOGIN_LAST_PASSWORD,"");
		EncriptarPassword encriptarPassword = new EncriptarPassword(password);
		String decrypterPassword = encriptarPassword.desencriptar();
		
		return decrypterPassword;
	}

	public static void forceLocalgisDatabaseURL(String forceLocalgisDatabaseURL2) {
		forceLocalgisDatabaseURL=forceLocalgisDatabaseURL2;
		
	}
}