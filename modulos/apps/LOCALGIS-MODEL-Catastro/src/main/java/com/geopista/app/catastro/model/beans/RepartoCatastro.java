/**
 * RepartoCatastro.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.geopista.app.AppContext;



public class RepartoCatastro implements Serializable
{
	public static final String TIPO_MOVIMIENTO_FINAL ="F";
	public static final String TIPO_MOVIMIENTO_ALTA ="A";
	public static final String TIPO_MOVIMIENTO_BAJA ="B";
	public static final String TIPO_MOVIMIENTO_MODIF ="M";
	public String TIPO_MOVIMIENTO = TIPO_MOVIMIENTO_FINAL;
    /**
     * Identificador del reparto
     */
    //private String idReparto;
    /**
     * Tipo de registro
     */
    //private String tipoMovimiento;
    /**
     * Datos de expediente
     */
    //private Expediente datosExpediente;
    
    /**
     * Identificador de la construcci�n o cultivo origen
     */
    private ReferenciaCatastral idOrigen;
    /**
     * IDentificador de la construcci�n destino
     */
    //private String idConstruccionDestino;    
    
    /**
     * Numero de cargo destino
     */
    //private String numCargoDestino;    
    
    /**
     * N�mero de orden del elemento de construccion a repartir
     */
    private String numOrdenConsRepartir;
    /**
     * N�mero de orden del elemento repartido
     */
    //private String numOrdenRepartido;
    /**
     * C�digo de subparcela de elemento a repartir
     */
    private String codSubparcelaElementoRepartir;
    /**
     * Calificacion catastral del cultivo a repartir
     */
    private String califCatastralElementoRepartir;
    
    /**
     * tipo de reparto: construcciones, cultivos o construccion en bienes
     */
    private String tipoReparto;
    
    /**
     * Porcentaje de reparto
     */
    private float porcentajeReparto;
    
    /**
     * C�digo de la delegaci�n
     */
    private String codDelegacion;
    
    /**
     * C�digo del municipio
     */
    private String codMunicipio;
    
    
    /**
     * Elemento repartido (s�lo se rellena si se encuentra el elemento
     * en la finca). Puede ser un cargo o un local/cultivo
     */
    //private Object elemRepartido;
    
    private ArrayList lstBienes;
    
    private ArrayList lstLocales;
    
    /**
     * Constructor por defecto
     *
     */
    public RepartoCatastro()
    {
        
    }
    
    
    
    
   


	public ArrayList getLstBienes() {
		return lstBienes;
	}







	public void setLstBienes(ArrayList lstBienes) {
		this.lstBienes = lstBienes;
	}







	public ArrayList getLstLocales() {
		return lstLocales;
	}







	public void setLstLocales(ArrayList lstLocales) {
		this.lstLocales = lstLocales;
	}







	public String getTIPO_MOVIMIENTO() {
		//return "F";
		return TIPO_MOVIMIENTO;
	}




	public void setTIPO_MOVIMIENTO(String tipo_movimiento) {
		TIPO_MOVIMIENTO = tipo_movimiento;
	}




	/**
     * @return Returns the datosExpediente.
     */
    /*public Expediente getDatosExpediente() {
        return datosExpediente;
    }*/
    
    
    /**
     * @param datosExpediente The datosExpediente to set.
     */
    /*public void setDatosExpediente(Expediente datosExpediente) {
        this.datosExpediente = datosExpediente;
    }*/
    
    
    
    
    /**
     * @return Returns the idOrigen.
     */
    public ReferenciaCatastral getIdOrigen() {
        return idOrigen;
    }
    
    
    /**
     * @param idOrigen The idOrigen to set.
     */
    public void setIdOrigen(ReferenciaCatastral idOrigen) {
        this.idOrigen = idOrigen;
    }
    
    
    /**
     * @return Returns the idReparto.
     */
    /*public String getIdReparto() {
        return idReparto;
    }*/
    
    
    /**
     * @param idReparto The idReparto to set.
     */
    /*public void setIdReparto(String idReparto) {
        this.idReparto = idReparto;
    }*/
    
    
    /**
     * @return Returns the porcentajeReparto.
     */
    public float getPorcentajeReparto() {
        return porcentajeReparto;
    }
    
    
    /**
     * @param porcentajeReparto The porcentajeReparto to set.
     */
    public void setPorcentajeReparto(float porcentajeReparto) {
        this.porcentajeReparto = porcentajeReparto;
    }
    
    
    /**
     * @return Returns the tipoMovimiento.
     */
    /*public String getTipoMovimiento() {
        return tipoMovimiento;
    }*/
    
    
    /**
     * @param tipoMovimiento The tipoMovimiento to set.
     */
    /*public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }*/
    
    /**
     * @return Returns the califCatastralElementoRepartir.
     */
    public String getCalifCatastralElementoRepartir()
    {
        return califCatastralElementoRepartir;
    }
    
    /**
     * @param califCatastralElementoRepartir The califCatastralElementoRepartir to set.
     */
    public void setCalifCatastralElementoRepartir(String califCatastralElementoRepartir)
    {
        this.califCatastralElementoRepartir = califCatastralElementoRepartir;
    }
    
    /**
     * @return Returns the codSubparcelaElementoRepartir.
     */
    public String getCodSubparcelaElementoRepartir()
    {
        return codSubparcelaElementoRepartir;
    }
    
    /**
     * @param codSubparcelaElementoRepartir The codSubparcelaElementoRepartir to set.
     */
    public void setCodSubparcelaElementoRepartir(String codSubparcelaElementoRepartir)
    {
        this.codSubparcelaElementoRepartir = codSubparcelaElementoRepartir;
    }
    
    /**
     * @return Returns the numOrdenConsRepartir.
     */
    public String getNumOrdenConsRepartir()
    {
        return numOrdenConsRepartir;
    }
    
    /**
     * @param numOrdenConsRepartir The numOrdenConsRepartir to set.
     */
    public void setNumOrdenConsRepartir(String numOrdenConsRepartir)
    {
        this.numOrdenConsRepartir = numOrdenConsRepartir;
    }
    
    /**
     * @return Returns the tipoReparto.
     */
    public String getTipoReparto()
    {
        return tipoReparto;
    }
    
    /**
     * @param tipoReparto The tipoReparto to set.
     */
    public void setTipoReparto(String tipoReparto)
    {
        this.tipoReparto = tipoReparto;
    }
    
    /**
     * @return Returns the numOrdenRepartido.
     */
    /*public String getNumOrdenRepartido()
    {
        return numOrdenRepartido;
    }*/
    
    /**
     * @param numOrdenRepartido The numOrdenRepartido to set.
     */
    /*public void setNumOrdenRepartido(String numOrdenRepartido)
    {
        this.numOrdenRepartido = numOrdenRepartido;
    }*/	
    
    
    /**
     * @return Returns the idConstruccionDestino.
     */
    /*public String getIdConstruccionDestino()
    {
        return idConstruccionDestino;
    }*/
    
    /**
     * @param idConstruccionDestino The idConstruccionDestino to set.
     */
    /*public void setIdConstruccionDestino(String idConstruccionDestino)
    {
        this.idConstruccionDestino = idConstruccionDestino;
    }*/
    
    
    /**
     * @return Returns the numCargoDestino.
     */
    /*public String getNumCargoDestino()
    {
        return numCargoDestino;
    }*/
    
    /**
     * @param numCargoDestino The numCargoDestino to set.
     */
    /*public void setNumCargoDestino(String numCargoDestino)
    {
        this.numCargoDestino = numCargoDestino;
    }*/
  
    public String getCodDelegacion() {
		return codDelegacion;
	}


	public void setCodDelegacion(String codDelegacion) {
		this.codDelegacion = codDelegacion;
	}




	public String getCodMunicipio() {
		return codMunicipio;
	}

	public void setCodMunicipio(String codMunicipio) {
		this.codMunicipio = codMunicipio;
	}

    public Boolean esLocal()
    {
        return new Boolean((numOrdenConsRepartir!=null&& !numOrdenConsRepartir.equalsIgnoreCase("")) &&
                (codSubparcelaElementoRepartir==null||(codSubparcelaElementoRepartir!=null &&codSubparcelaElementoRepartir.equalsIgnoreCase(""))
                        && (califCatastralElementoRepartir==null||(califCatastralElementoRepartir!=null&&califCatastralElementoRepartir.equalsIgnoreCase("")))));
    }

    public Boolean esCultivo()
    {
        return new Boolean(!esLocal().booleanValue());
    }


    /**
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }

    /**
     * @return Returns the elemRepartido.
     */
    /*public Object getElemRepartido()
    {
        return elemRepartido;
    }*/

    /**
     * @param elemRepartido The elemRepartido to set.
     */
    /*public void setElemRepartido(Object elemRepartido)
    {
        this.elemRepartido = elemRepartido;
    }*/
    
    public RepartoCatastro clone(RepartoCatastro reparto)
    {
    	RepartoCatastro repartoNuevo = new RepartoCatastro();
    	
    	repartoNuevo.setCalifCatastralElementoRepartir(reparto.getCalifCatastralElementoRepartir());
    	repartoNuevo.setCodDelegacion(reparto.getCodDelegacion());
    	repartoNuevo.setCodMunicipio(reparto.getCodMunicipio());
    	repartoNuevo.setCodSubparcelaElementoRepartir(reparto.getCodSubparcelaElementoRepartir());
    	//repartoNuevo.setDatosExpediente(reparto.getDatosExpediente());
    	//repartoNuevo.setElemRepartido(reparto.getElemRepartido());
    	//repartoNuevo.setIdConstruccionDestino(reparto.getIdConstruccionDestino());
    	repartoNuevo.setIdOrigen(reparto.getIdOrigen());
    	//repartoNuevo.setIdReparto(reparto.getIdReparto());
    	//repartoNuevo.setNumCargoDestino(reparto.getNumCargoDestino());
    	repartoNuevo.setNumOrdenConsRepartir(repartoNuevo.getNumOrdenConsRepartir());
    	//repartoNuevo.setNumOrdenRepartido(reparto.getNumOrdenRepartido());
    	repartoNuevo.setPorcentajeReparto(reparto.getPorcentajeReparto());
    	repartoNuevo.setTIPO_MOVIMIENTO(reparto.getTIPO_MOVIMIENTO());
    	
    	return repartoNuevo;
    }
    
public boolean isCatastroTemporal(){
		
		if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
			boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
			if(isCatTemporal){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
		//return true;
	}
	public boolean isNotCatastroTemporal(){
		if( AppContext.getApplicationContext().getBlackboard().get("catastroTemporal") != null ){
			boolean isCatTemporal = (Boolean)AppContext.getApplicationContext().getBlackboard().get("catastroTemporal");
			if(isCatTemporal){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return true;
		}
		
		//return false;
	}
	
	public Boolean isElemModificado() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) || 
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF) ||
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Boolean isRepartoAltaModif(){
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean repartoAltaModif = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_ALTA) || 
				TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_MODIF)){
			repartoAltaModif= true;
		}
		return repartoAltaModif;
	}
	
	public Boolean isRepartoElim() {
		//para generar el fichero fin de entrada de un expdiente orientado a variaciones
		Boolean repartoElim = false;
		if(TIPO_MOVIMIENTO.equals(TIPO_MOVIMIENTO_BAJA)){
			repartoElim = true;
		}
		return repartoElim;
	}
}
