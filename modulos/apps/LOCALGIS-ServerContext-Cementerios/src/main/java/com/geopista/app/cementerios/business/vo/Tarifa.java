/**
 * Tarifa.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.cementerios.business.vo;

public class Tarifa {

	/**
	 * This field was generated by Apache iBATIS ibator. This field corresponds to the database column cementerio.tarifa.id
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	private Integer id;
	/**
	 * This field was generated by Apache iBATIS ibator. This field corresponds to the database column cementerio.tarifa.concepto
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	private String concepto;
	/**
	 * This field was generated by Apache iBATIS ibator. This field corresponds to the database column cementerio.tarifa.tipo_tarifa
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	private String tipoTarifa;
	/**
	 * This field was generated by Apache iBATIS ibator. This field corresponds to the database column cementerio.tarifa.tipo_calculo
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	private String tipoCalculo;
	/**
	 * This field was generated by Apache iBATIS ibator. This field corresponds to the database column cementerio.tarifa.id_cementerio
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	private Integer idCementerio;
	/**
	 * This field was generated by Apache iBATIS ibator. This field corresponds to the database column cementerio.tarifa.categoria
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	private Integer categoria;
	/**
	 * This field was generated by Apache iBATIS ibator. This field corresponds to the database column cementerio.tarifa.precio
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	private String precio;

	/**
	 * This method was generated by Apache iBATIS ibator. This method returns the value of the database column cementerio.tarifa.id
	 * @return  the value of cementerio.tarifa.id
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method sets the value of the database column cementerio.tarifa.id
	 * @param id  the value for cementerio.tarifa.id
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method returns the value of the database column cementerio.tarifa.concepto
	 * @return  the value of cementerio.tarifa.concepto
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method sets the value of the database column cementerio.tarifa.concepto
	 * @param concepto  the value for cementerio.tarifa.concepto
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method returns the value of the database column cementerio.tarifa.tipo_tarifa
	 * @return  the value of cementerio.tarifa.tipo_tarifa
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public String getTipoTarifa() {
		return tipoTarifa;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method sets the value of the database column cementerio.tarifa.tipo_tarifa
	 * @param tipoTarifa  the value for cementerio.tarifa.tipo_tarifa
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method returns the value of the database column cementerio.tarifa.tipo_calculo
	 * @return  the value of cementerio.tarifa.tipo_calculo
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public String getTipoCalculo() {
		return tipoCalculo;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method sets the value of the database column cementerio.tarifa.tipo_calculo
	 * @param tipoCalculo  the value for cementerio.tarifa.tipo_calculo
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public void setTipoCalculo(String tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method returns the value of the database column cementerio.tarifa.id_cementerio
	 * @return  the value of cementerio.tarifa.id_cementerio
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public Integer getIdCementerio() {
		return idCementerio;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method sets the value of the database column cementerio.tarifa.id_cementerio
	 * @param idCementerio  the value for cementerio.tarifa.id_cementerio
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public void setIdCementerio(Integer idCementerio) {
		this.idCementerio = idCementerio;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method returns the value of the database column cementerio.tarifa.categoria
	 * @return  the value of cementerio.tarifa.categoria
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public Integer getCategoria() {
		return categoria;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method sets the value of the database column cementerio.tarifa.categoria
	 * @param categoria  the value for cementerio.tarifa.categoria
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public void setCategoria(Integer categoria) {
		this.categoria = categoria;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method returns the value of the database column cementerio.tarifa.precio
	 * @return  the value of cementerio.tarifa.precio
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public String getPrecio() {
		return precio;
	}

	/**
	 * This method was generated by Apache iBATIS ibator. This method sets the value of the database column cementerio.tarifa.precio
	 * @param precio  the value for cementerio.tarifa.precio
	 * @ibatorgenerated  Tue Jul 05 08:57:18 CEST 2011
	 */
	public void setPrecio(String precio) {
		this.precio = precio;
	}
}