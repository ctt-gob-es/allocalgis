/**
 * AbstractSymbolizer.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

/**
 * This is the basis of all symbolizers. It defines the method <tt>getGeometry</tt> that's common
 * to all symbolizers.
 * <p>
 * ----------------------------------------------------------------------
 * </p>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp</a>
 * @version $Revision: 1.1 $ $Date: 2011/09/19 13:48:12 $
 */

public abstract class AbstractSymbolizer implements Symbolizer {
    protected double maxDenominator = 9E99;

    protected double minDenominator = 0;

    protected String responsibleClass = null;

    /**
     * default constructor
     */
    AbstractSymbolizer() {
    }

    /**
     * constructor initializing the class with the <Symbolizer>
     */
    AbstractSymbolizer(String resonsibleClass ) {
        setResponsibleClass( resonsibleClass );
    }

    /**
     * @return the MinScaleDenominator
     */
    public double getMinScaleDenominator() {
        return minDenominator;
    }

    /**
     * @param minDenominator
     *            the MinScaleDenominator
     */
    public void setMinScaleDenominator( double minDenominator ) {
        this.minDenominator = minDenominator;
    }

    /**
     * @return the MaxScaleDenominator
     */
    public double getMaxScaleDenominator() {
        return maxDenominator;
    }

    /**
     * @param maxDenominator
     *            the MaxScaleDenominator
     */
    public void setMaxScaleDenominator( double maxDenominator ) {
        this.maxDenominator = maxDenominator;
    }

    /**
     * returns the name of a class that will be used for rendering the current symbolizer. This
     * enables a user to define his own rendering class (DisplayElement) for a symbolizer to realize
     * styles/renderings that can't be defined using SLD at the moment.<BR>
     * The returned class must extend
     * org.deegree_impl.graphics.displayelements.GeometryDisplayElement_Impl<BR>
     * For default the method returns the deegree default class name for rendering the current
     * symbolizer.
     * 
     * @return the name of a class that will be used for rendering the current symbolizer.
     * 
     */
    public String getResponsibleClass() {
        return responsibleClass;
    }

    /**
     * sets a class that will be used for rendering the current symbolizer. This enables a user to
     * define his own rendering class (DisplayElement) for a symbolizer to realize styles/renderings
     * that can't be defined using SLD at the moment.<BR>
     * The passed class must extend
     * org.deegree_impl.graphics.displayelements.GeometryDisplayElement_Impl
     * 
     * @param responsibleClass
     * 
     */
    public void setResponsibleClass( String responsibleClass ) {
        this.responsibleClass = responsibleClass;
    }

}
