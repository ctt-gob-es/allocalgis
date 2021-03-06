/**
 * ValidarEsquemaPanel.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.dialogs.FileValidatorSelectPanel;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtilsCatastro;
import com.geopista.app.editor.GeopistaFiltroFicheroFilter;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ValidarEsquemaPanel extends JPanel implements WizardPanel
{
    private boolean permiso = true;
    
    private AppContext application = (AppContext) AppContext.getApplicationContext();
    private Blackboard blackboard = application.getBlackboard();
    private WizardContext wizardContext; 
    
    private StringBuffer strBuf = new StringBuffer();  
    private String mensajeValidacion = new String();  
    
    private JComboBox jComboBoxFileType = new JComboBox();
    private JComboBox jComboBoxDatosImportar = new JComboBox();
    private JEditorPane jEditorPaneErrores = new JEditorPane();
    private JFileChooser fc = null;
    private JTextField jTextFieldFileName = null;
    
    //  Variables utilizadas para las validaciones
    private boolean continuar = false;       
    
    /**
     * Nombre de la etiqueta ra�z del xml que se espera importar
     */
    private static final String TIPO_FICHERO_FE = "finentrada";
    private static final String TIPO_FICHERO_VA = "varpad";
    
    public static final int DIM_X=700;
    public static final int DIM_Y=450;
    
    public ValidarEsquemaPanel()
    {   
        try
        {     
            Locale loc=I18N.getLocaleAsObject();         
            ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Importacioni18n",loc,this.getClass().getClassLoader());
            I18N.plugInsResourceBundle.put("Importacion",bundle);
            jbInit();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception
    {  
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(DIM_X, DIM_Y));
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application.getMainFrame(), null);
        
        progressDialog.setTitle(I18N.get("Importacion","CargandoDatosIniciales"));
        progressDialog.report(I18N.get("Importacion","CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter()
                {
            public void componentShown(ComponentEvent e)
            {                
                // Wait for the dialog to appear before starting the
                // task. Otherwise
                // the task might possibly finish before the dialog
                // appeared and the
                // dialog would never close. [Jon Aquino]
                new Thread(new Runnable()
                        {
                    

					public void run()
                    {
                        try
                        {
                        	FileValidatorSelectPanel panel = new FileValidatorSelectPanel();
                            add(panel,  
                                    new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH, new Insets(10,10,10,10) ,0,0));
                            
                            jComboBoxDatosImportar = panel.getJComboBoxDatosImportar();
//                            jComboBoxDatosImportar.addItem(I18N.get("Importacion","importar.asistente.finentrada.tipo"));
                            jComboBoxDatosImportar.addItem(TIPO_FICHERO_FE);
//                            jComboBoxDatosImportar.addItem(I18N.get("Importacion","importar.asistente.varpad.tipo"));
                            jComboBoxDatosImportar.addItem(TIPO_FICHERO_VA);
                            jTextFieldFileName = panel.getJTextFieldFicheroImportar();
                            jComboBoxFileType = panel.getJComboBoxTipoFichero();
                            jComboBoxFileType.addItem(I18N.get("Importacion","importar.general.tiposfichero.xml"));                            
                            jEditorPaneErrores = panel.getJEditorPaneErrores();
                            
                            panel.getLabelImagen().setIcon(IconLoader.icon(MainCatastro.BIG_PICTURE_LOCATION));
                            panel.getJButtonFicheroImportar().addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {
                                    btnOpen_actionPerformed(e);
                                }
                                    });
                            

                            jComboBoxDatosImportar.addActionListener(new ActionListener()
                            {
                            	public void actionPerformed(ActionEvent e)
                            	{
                            		jComboBoxDatosImportarActionPerformed(e);
                            	}
                            });

                            panel.getJComboBoxTipoFichero().addActionListener(new ActionListener()
                                    {
                                public void actionPerformed(ActionEvent e)
                                {
                                    cmbTipoFichero_actionPerformed(e);
                                }
                                    });
                            
                        } 
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        } 
                        finally
                        {
                            progressDialog.setVisible(false);
                        }
                    }
                        }).start();
            }
                });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
        
    }
    
    public void enteredFromLeft(Map dataMap)
    {
        if(!application.isLogged())
        {
            application.login();
        }
        if(!application.isLogged())
        {
            wizardContext.cancelWizard();
            return;
        }        
        
        try
        {
            // Iniciamos la ayuda
        	String helpHS = "help/catastro/importadores/importadoresHelp_es.hs";
            ClassLoader c1 = this.getClass().getClassLoader();
            URL hsURL = HelpSet.findHelpSet(c1, helpHS);
            HelpSet hs = new HelpSet(null, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            // fin de la ayuda
            hb.enableHelpKey(this,"FinSalida", hs);
        } 
        catch (Exception excp)
        {
            excp.printStackTrace();
        }
       
    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception
    {
        blackboard.put(ImportarUtils.FILE_TO_IMPORT, jTextFieldFileName.getText());        
    }
    
    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
    public void add(InputChangedListener listener)
    {
        
    }
    
    public void remove(InputChangedListener listener)
    {
        
    }
    
    public String getTitle()
    {
        return this.getName();
    }
    
    public String getID()
    {
        return "1";
    }
    
    public String getInstructions()
    {
        return " ";
    }
    
    public boolean isInputValid()
    {        
        if (!permiso)
        {
            JOptionPane.showMessageDialog(application.getMainFrame(), application
                    .getI18nString("NoPermisos"));
            return false;
        } 
        else
        {
            if (!continuar)            
                return false;             
            else                         
                if ((jTextFieldFileName.getText().length()) == 0)                
                    return false;                 
                else                
                    return true;   
        }
    }
    
        
    public void setWizardContext(WizardContext wd)
    {
        wizardContext = wd;
    }
    
    private void jComboBoxDatosImportarActionPerformed(ActionEvent e)
    { 
    	if (jComboBoxDatosImportar.getSelectedItem()!=null){
    		String typeValidation = jComboBoxDatosImportar.getSelectedItem().toString();
    		AppContext.getApplicationContext().getBlackboard().put("typeValidation", typeValidation);
    	}
    }
    
    private void btnOpen_actionPerformed(ActionEvent e)
    {        
        // Se inicializa para cada proceso de importacion
        continuar = false;       
        
        GeopistaFiltroFicheroFilter filter = new GeopistaFiltroFicheroFilter();
        
        int cmbIndex = jComboBoxFileType.getSelectedIndex();
        if ((cmbIndex == 0) || (cmbIndex == 1))
        {
            filter.addExtension("XML");
            filter.setDescription(I18N.get("Importacion","importar.general.tiposfichero.xml"));
        }
        fc = new JFileChooser();
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false); // QUITA LA OPCION ALL FILES(*.*)
        String lastFolder=UserPreferenceStore.getUserPreference(ImportarUtils.LAST_IMPORT_DIRECTORY, null, false);
        File currentDirectory=null;
        if (lastFolder!=null)
        	currentDirectory=new File(lastFolder);
                
        fc.setCurrentDirectory(currentDirectory);
        
        int returnVal = fc.showOpenDialog(this);
        
        UserPreferenceStore.setUserPreference(ImportarUtils.LAST_IMPORT_DIRECTORY, fc.getCurrentDirectory().getAbsolutePath());
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            // Cargamos el fichero que hemos obtenido
            String fichero;
            fichero = fc.getSelectedFile().getPath();
            jTextFieldFileName.setText(fichero); 
            
            jEditorPaneErrores.removeAll();
            strBuf = new StringBuffer();
            
            strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
            .append(I18N.get("Importacion","importar.general.proceso.comenzar"))
            .append(" ").append(fc.getSelectedFile().getName())
            .append(ImportarUtils.HTML_FIN_PARRAFO);
            
            strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
            .append(I18N.get("Importacion","importar.general.proceso.espera"))
            .append(ImportarUtils.HTML_FIN_PARRAFO);
            
            strBuf.append(ImportarUtils.HTML_NUEVO_PARRAFO)
            .append(I18N.get("Importacion","importar.general.proceso.verificando"))
            .append(ImportarUtils.HTML_FIN_PARRAFO);
            
            jEditorPaneErrores.setText(strBuf.toString());
            strBuf=new StringBuffer();;
            
            final TaskMonitorDialog progressDialog = new TaskMonitorDialog(application
                    .getMainFrame(), null);
            
            progressDialog.setTitle(I18N.get("Importacion","validar.general.proceso.validando"));
            progressDialog.report(I18N.get("Importacion","validar.general.proceso.validando"));
            progressDialog.addComponentListener(new ComponentAdapter()
                    {
                public void componentShown(ComponentEvent e)
                {
                    
                    // Wait for the dialog to appear before starting the
                    // task. Otherwise
                    // the task might possibly finish before the dialog
                    // appeared and the
                    // dialog would never close. [Jon Aquino]
                    new Thread(new Runnable()
                            {
                        public void run()
                        {                            
                            try
                            {
                                continuar = true;

                                if (jComboBoxDatosImportar.getSelectedItem()!=null){
                                	String typeValidation = jComboBoxDatosImportar.getSelectedItem().toString();

                                	mensajeValidacion = ImportarUtilsCatastro.validateLowSAXdocumentWithXSD(fc.getSelectedFile().getPath(), typeValidation);
                                	//Comprobar que el XML se valida con el esquema dado y corresponde al tipo finsalida
                                }
                                else{
                                	mensajeValidacion = I18N.get("Importacion","importar.general.validacion.selecttype");
                                }
 
                            } 
                            catch (Exception e)
                            {
                                continuar = false;
                            } 
                            finally
                            {
                                progressDialog.setVisible(false);
                            }
                        }
                            }).start();
                }
                    });
            GUIUtil.centreOnWindow(progressDialog);
            progressDialog.setVisible(true);         
            
            if (mensajeValidacion.trim().length()==0){
              strBuf.append(ImportarUtils.HTML_VERDE)
              .append(I18N.get("Importacion","importar.general.validacion.correcta"))
              .append(ImportarUtils.HTML_SALTO).append(ImportarUtils.HTML_SALTO)
              .append(I18N.get("Importacion","importar.general.validacion.finalizada"))
              .append(ImportarUtils.HTML_FIN_PARRAFO);  
            }
            else{
            	strBuf.append(ImportarUtils.HTML_ROJO).append(mensajeValidacion).append(ImportarUtils.HTML_FIN_PARRAFO);
            }
            jEditorPaneErrores.setText(strBuf.toString());
            wizardContext.inputChanged();
        }        
    }
    
    private void cmbTipoFichero_actionPerformed(ActionEvent e)
    {
        jTextFieldFileName.setText("");
    }
    
    
    public void exiting()
    {   
    }

	public String getNextID() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNextID(String nextID) {
		// TODO Auto-generated method stub
		
	}
}  
