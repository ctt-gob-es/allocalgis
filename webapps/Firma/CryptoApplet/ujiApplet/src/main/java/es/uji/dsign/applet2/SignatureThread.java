package es.uji.dsign.applet2;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.security.PrivateKey;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLHandshakeException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import es.uji.dsign.applet2.Exceptions.SignatureAppletException;
import es.uji.dsign.crypto.ISignFormatProvider;
import es.uji.dsign.crypto.SignatureOptions;
import es.uji.dsign.crypto.X509CertificateHandler;
import es.uji.dsign.crypto.XAdESCoSignatureFactory;
import es.uji.dsign.crypto.XAdESSignatureFactory;
import es.uji.dsign.crypto.keystore.IKeyStoreHelper;
import es.uji.dsign.util.Base64;
import es.uji.dsign.util.HexEncoder;
import es.uji.dsign.util.i18n.LabelManager;
import es.uji.dsign.applet2.io.CMSLocalFileInputParams;
import es.uji.dsign.applet2.io.CMSLocalFileOutputParams;
import es.uji.dsign.applet2.io.FileInputParams;
import es.uji.dsign.applet2.io.FileOutputParams;
import es.uji.dsign.applet2.io.InputParams;
import es.uji.dsign.applet2.io.OutputParams;
import es.uji.dsign.applet2.io.PDFLocalFileInputParams;
import es.uji.dsign.applet2.io.PDFLocalFileOutputParams;
import es.uji.dsign.applet2.io.RAWLocalFileInputParams;
import es.uji.dsign.applet2.io.RAWLocalFileOutputParams;
import es.uji.dsign.applet2.io.XADESLocalFileInputParams;
import es.uji.dsign.applet2.io.XADESLocalFileOutputParams;


public class SignatureThread extends Thread
{
	private MainWindow _mw= null;
	private int _end_percent= 0;
	private int _ini_percent= 0, _step= 0;
	private Method callback;

	public SignatureThread(String str)
	{
		super(str);
	}

	public void setPercentRange(int ini_percent, int end_percent, int step){
		this._step= step;
		this._ini_percent= ini_percent;
		this._end_percent= end_percent;
	} 

	public void setCallbackMethod(Method m){
		callback= m;
	}

	public void run()
	{

		IKeyStoreHelper iksh;
		guiInitialize();
		JLabel infoLabelField= _mw.getInformationLabelField();
		
		infoLabelField.setText(LabelManager.get("COMPUTING_SIGNATURE"));
		
		int inc= (this._end_percent - this._ini_percent)/10;

		try{
			X509CertificateHandler selectedNode;
			try{
				selectedNode= (X509CertificateHandler) ((DefaultMutableTreeNode) _mw.jTree.getLastSelectedPathComponent()).getUserObject();
			}
			catch(NullPointerException e){
				throw new SignatureAppletException("ERROR_CERTIFICATE_NOT_SELECTED");
			} 
			
			if (!selectedNode.isDigitalSignatureCertificate() && !selectedNode.isNonRepudiationCertificate())
			{
				guiFinalize(false);
				throw new SignatureAppletException("ERROR_CERTIFICATE_USE");
			} 
			try{
			    selectedNode.getCertificate().checkValidity();
			}
			catch(CertificateException cex){
				 int selection = JOptionPane.showOptionDialog(_mw.getMainFrame(),
					   LabelManager.get("LABEL_CERTIFICATE_EXPIRED"),  
					   LabelManager.get("LABEL_CERTIFICATE_EXPIRED_TITLE"),
					   JOptionPane.YES_NO_OPTION,
					   JOptionPane.QUESTION_MESSAGE, null, 
					   new String[] {"Yes", "No"}, "No");
				  	if (selection == JOptionPane.NO_OPTION) {
						guiFinalize(false);
						throw new SignatureAppletException("ERROR_CERTIFICATE_EXPIRED");
					}
			} 
			
			iksh= selectedNode.getKeyStore();
			if (iksh != null){	
				try
				{
					iksh.load(_mw.getPasswordTextField().getText().toCharArray());
					
					//TODO: Research: Some problems of codification with 1.6 jvm and 
					//      JPasswordField.
					//iksh.load(_mw.getPasswordField().getPassword());
				}
				catch (Exception e)
				{
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(os);
					e.printStackTrace(ps);
					String stk = new String(os.toByteArray()).toLowerCase();
					if ( stk.indexOf("incorrect") > -1 ){
						guiFinalize(false);
						throw new SignatureAppletException("ERROR_INCORRECT_PWD");
					}
					else
						infoLabelField.setText("Unexpected error!!!");
					e.printStackTrace();
				}
				System.out.println("Certificate Alias: " +  iksh.getAliasFromCertificate(selectedNode.getCertificate()));
			}
			else{
				guiFinalize(false);
				throw new SignatureAppletException("ERR_GET_KEYSTORE");
			}

			_mw.getGlobalProgressBar().setValue(_ini_percent + inc);

			

			_mw.getGlobalProgressBar().setValue(_ini_percent + 2*inc);

			//Obtenemos los parametros de salida. La diferencia con la version inicial
			//es que en este momento sabemos el tipo de firma y podemos definir como se vuelca
			//la salida (fjgarcia)
			if (_mw.getSignFormat().equalsIgnoreCase("PDF")){
				System.out.println("Firmando en Formato PDF");
                PDFLocalFileInputParams input = new PDFLocalFileInputParams(_mw.getAppHandler().getSignatureApplet());
                PDFLocalFileOutputParams output = new PDFLocalFileOutputParams(input);        
                _mw.getAppHandler().setInput(input);
                _mw.getAppHandler().setOutput(output);
                _mw.getAppHandler().setSignatureOutputFormat("PDF");
			}
			else if (_mw.getSignFormat().equalsIgnoreCase("RAW")){
				System.out.println("Firmando en Formato RAW");
                RAWLocalFileInputParams input = new RAWLocalFileInputParams(_mw.getAppHandler().getSignatureApplet());
                RAWLocalFileOutputParams output = new RAWLocalFileOutputParams(input);        
                _mw.getAppHandler().setInput(input);
                _mw.getAppHandler().setOutput(output);	
                _mw.getAppHandler().setSignatureOutputFormat("RAW");
			}
			else if (_mw.getSignFormat().equalsIgnoreCase("CMS")){
				System.out.println("Firmando en Formato CMS");
                CMSLocalFileInputParams input = new CMSLocalFileInputParams(_mw.getAppHandler().getSignatureApplet());
                CMSLocalFileOutputParams output = new CMSLocalFileOutputParams(input);        
                _mw.getAppHandler().setInput(input);
                _mw.getAppHandler().setOutput(output);	
                _mw.getAppHandler().setSignatureOutputFormat("CMS");
			}			
			else{ //Default XADES
				System.out.println("Firmando en Formato Xades");
                XADESLocalFileInputParams input = new XADESLocalFileInputParams(_mw.getAppHandler().getSignatureApplet());
                XADESLocalFileOutputParams output = new XADESLocalFileOutputParams(input);        
                _mw.getAppHandler().setInput(input);
                _mw.getAppHandler().setOutput(output);	
                _mw.getAppHandler().setSignatureOutputFormat("XADES_MITYC");                
                

			}	
			_mw.setInfoCertificate("");
			
			InputParams inputParams = (InputParams)_mw._aph.getInput(); 
			OutputParams outputParams = (OutputParams) _mw.getAppHandler().getOutputParams();

			_mw.getGlobalProgressBar().setValue(_ini_percent + 3*inc);
			
			System.out.println("!!!!!!FORMATO FIRMA SELECCIONADO:"+_mw.getSignFormat());
			System.out.println("!!!!!!CLASE PARA FIRMA:"+_mw.getAppHandler().getSignFormat());
			
			// Creating an instance of the signature formater: CMS, XAdES, etc
			Class sf = Class.forName(_mw.getAppHandler().getSignFormat());
			ISignFormatProvider signer = (ISignFormatProvider) sf.newInstance();

			if (_mw.getAppHandler().getSignFormat().equals("es.uji.dsign.crypto.XAdESSignatureFactory") || 
					_mw.getAppHandler().getSignFormat().equals("es.uji.dsign.crypto.XAdESCoSignatureFactory"))
			{
				String[] roles= _mw.getAppHandler().getSignerRole();
				String sr= "UNSET"; 
				if (this._step < roles.length){
					sr=roles[this._step];
				}
				String fname= (_mw.getAppHandler().getXadesFileName() != null)? _mw.getAppHandler().getXadesFileName(): "UNSET";
				String fmimetype= (_mw.getAppHandler().getXadesFileMimeType() != null)? _mw.getAppHandler().getXadesFileMimeType(): "application/binary";
			
				if (_mw.getAppHandler().getSignFormat().equals("es.uji.dsign.crypto.XAdESSignatureFactory")){
					XAdESSignatureFactory xs= (XAdESSignatureFactory) signer;
					xs.setSignerRole(sr);
					xs.setXadesFileName(fname);
					xs.setXadesFileMimeType(fmimetype); 
				}
				else{
					XAdESCoSignatureFactory xs= (XAdESCoSignatureFactory) signer;
					xs.setSignerRole(sr);
					xs.setXadesFileName(fname);
					xs.setXadesFileMimeType(fmimetype); 
				}
			}

			_mw.getGlobalProgressBar().setValue(_ini_percent + 4*inc);
			if (_mw.jTree.getLastSelectedPathComponent() != null)
			{
				X509CertificateHandler xcert;
				try{
					xcert = (X509CertificateHandler) ((DefaultMutableTreeNode) _mw.jTree.getLastSelectedPathComponent()).getUserObject();
				}
				catch (NullPointerException e){
					guiFinalize(false);
					throw new SignatureAppletException("ERROR_CERTIFICATE_NOT_SELECTED");

				}
				if (xcert.isDigitalSignatureCertificate() || 
						(xcert.isEmailProtectionCertificate() &&
								_mw.getAppHandler().getSignFormat().equals("es.uji.dsign.crypto.CMSSignatureFactory")));
				{
					ByteArrayOutputStream ot = new ByteArrayOutputStream();

					byte[] in;

					String encoding= _mw.getAppHandler().getEncoding() != null ? _mw._aph.getEncoding() : "plain";


					_mw.getGlobalProgressBar().setValue(_ini_percent + 5*inc);
					if ( encoding.toLowerCase().equals("hex") ){
						HexEncoder h= new HexEncoder();
						h.decode(new String(inputParams.getSignData()), ot);
						in= ot.toByteArray();
					}
					else if ( encoding.toLowerCase().equals("base64") ){
						in= Base64.decode(inputParams.getSignData());
					}
					else{
						in= inputParams.getSignData();
					}

					if (_mw.isShowSignatureEnabled()){
						int sel=JOptionPane.showConfirmDialog(_mw.getMainFrame(),_mw.getShowDataScrollPane(in), LabelManager.get("LABEL_SHOW_DATA_WINDOW") , JOptionPane.OK_CANCEL_OPTION);
						if ( sel != JOptionPane.OK_OPTION ){
							_mw.getAppHandler().callJavaScriptCallbackFunction(_mw.getAppHandler().getJsSignCancel(), new String[] {});
							guiFinalize(false);
							return;
						}
					}
					
					byte[] sig= null;

					_mw.getGlobalProgressBar().setValue(_ini_percent + 6*inc);

					IKeyStoreHelper kAux= xcert.getKeyStore();
					
					try{ 
						//Set up the data for the signature handling.
						SignatureOptions sigOpt= new SignatureOptions();
						sigOpt.setToSignByteArray(in);
						sigOpt.setCertificate(xcert.getCertificate());
						sigOpt.setPrivateKey((PrivateKey)kAux.getKey(xcert.getAlias()));
						sigOpt.setProvider(kAux.getProvider());
						sigOpt.setFileName(inputParams.getSignedFile());
						sigOpt.setSignFormat(_mw.getSignFormat());
						
						sig= signer.formatSignature(sigOpt);
					}
					catch(Exception e){
						e.printStackTrace();
						System.out.println("Message: " + e.getMessage());
						throw new SignatureAppletException("ERROR_COMPUTING_SIGNATURE");
					}
					
					if ( sig == null )
					{
						infoLabelField.setText(LabelManager.get("ERROR_COMPUTING_SIGNATURE") +": " + signer.getError());
						guiFinalize(false);
						_mw.getAppHandler().getInputParams().flush();
						throw new SignatureAppletException(LabelManager.get("ERROR_COMPUTING_SIGNATURE")
								                          + " :: " + signer.getError(),false);
					}

					_mw.getGlobalProgressBar().setValue(_ini_percent + 7*inc);
					//System.out.println("Setting input data ... ");
					
					if ( sig != null )	
						try{
							outputParams.setSignData(sig);
						}
						catch(Exception e){
							System.out.println("Exception launch");
							e.printStackTrace();
							throw new SignatureAppletException("ERROR_CANNOT_SET_OUTPUT_DATA");
						}
					else 	
						System.out.println("ERROR!!! al calcular la firma");
				}
				_mw.getGlobalProgressBar().setValue(_ini_percent + 8*inc);
			}
			_mw.getGlobalProgressBar().setValue(_ini_percent + 10*inc);

			guiFinalize(true);

			callback.invoke(null, null);
		}
		catch (SSLHandshakeException e){
			infoLabelField.setText(LabelManager.get("ERROR_SSL") +": " + e.getMessage());
			e.printStackTrace();
			try {
				guiFinalize(false);
			} catch (Exception e1) {
				infoLabelField.setText(LabelManager.get("ERROR_CANNOT_CLOSE_WINDOW"));
				e1.printStackTrace();
			}		
		}
		catch(ClassCastException e){
			e.printStackTrace();
			infoLabelField.setText(LabelManager.get("ERROR_CERTIFICATE_NOT_SELECTED"));
			try {
				guiFinalize(false);
			} catch (Exception e1) {
				infoLabelField.setText(LabelManager.get("ERROR_CANNOT_CLOSE_WINDOW"));
			}
		}
		catch(NullPointerException e){
			e.printStackTrace();
			infoLabelField.setText(LabelManager.get("ERROR_COMPUTING_SIGNATURE") + ": "+ e.getMessage());
			try {
				guiFinalize(false);
			} catch (Exception e1) {
				infoLabelField.setText(LabelManager.get("ERROR_CANNOT_CLOSE_WINDOW"));
			}
		}
		catch(IOException e){
			infoLabelField.setText(LabelManager.get("ERROR_INPUT_SOURCE"));
			e.printStackTrace();
			try {
				guiFinalize(false);
			} catch (Exception e1) {
				infoLabelField.setText(LabelManager.get("ERROR_CANNOT_CLOSE_WINDOW"));
				e1.printStackTrace();
			}
		}
		catch(Exception e){
			e.printStackTrace();
			infoLabelField.setText(e.getMessage());
			try {
				guiFinalize(false);
			} catch (Exception e1) {
				infoLabelField.setText(LabelManager.get("ERROR_CANNOT_CLOSE_WINDOW"));
				e1.printStackTrace();
			}		
			_mw.getAppHandler().getInputParams().flush();
		}
	}


	private void guiInitialize(){

		if (_mw != null){
			_mw.getInformationLabelField().setText(LabelManager.get("COMPUTING_SIGNATURE"));
			_mw.SignButton.setEnabled(false);
			_mw.jTree.setEnabled(false);

			_mw.getGlobalProgressBar().setIndeterminate(false);
			_mw.getGlobalProgressBar().setVisible(true);
			_mw.getGlobalProgressBar().setStringPainted(true);
		}
	}

	private void guiFinalize(boolean resultCorrect) 
	throws Exception{
		if (_mw != null){
			if (resultCorrect){ 
				JOptionPane.showMessageDialog(_mw.getMainFrame(), LabelManager.get("SIGN_PROCESS_OK"), "", JOptionPane.INFORMATION_MESSAGE);
				_mw.getAppHandler().getOutputParams().signOk();
			}
			_mw.getGlobalProgressBar().setVisible(false);
			_mw.jTree.setEnabled(true);
			_mw.SignButton.setEnabled(true);

			_mw.getShowSignatureCheckBox().setVisible(true);

		}
		this._ini_percent= 0;
		this._end_percent= 100;
		
		if (resultCorrect){
			_mw.getAppHandler().getOutputParams().signOk();
		}
	} 

	public void setMainWindow(MainWindow mw){
		_mw= mw;
	}

}
