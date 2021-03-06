/**
 * ProcedureDefaultsDAOImpl.java
 * � MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.ProcedureDefaultsDAO;
import com.localgis.web.core.model.ProcedureDefaults;

public class ProcedureDefaultsDAOImpl extends SqlMapDaoTemplate implements ProcedureDefaultsDAO {

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table geowfst.procedure_defaults
     *
     * @ibatorgenerated Thu Mar 14 10:24:30 CET 2013
     */
    public ProcedureDefaultsDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table geowfst.procedure_defaults
     *
     * @ibatorgenerated Thu Mar 14 10:24:30 CET 2013
     */
    public void deleteByPrimaryKey(String id) {
        ProcedureDefaults key = new ProcedureDefaults();
        key.setId(id);
        delete("geowfst_procedure_defaults.ibatorgenerated_deleteByPrimaryKey", key);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table geowfst.procedure_defaults
     *
     * @ibatorgenerated Thu Mar 14 10:24:30 CET 2013
     */
    public void insert(ProcedureDefaults record) {
        insert("geowfst_procedure_defaults.ibatorgenerated_insert", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table geowfst.procedure_defaults
     *
     * @ibatorgenerated Thu Mar 14 10:24:30 CET 2013
     */
    public void insertSelective(ProcedureDefaults record) {
        insert("geowfst_procedure_defaults.ibatorgenerated_insertSelective", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table geowfst.procedure_defaults
     *
     * @ibatorgenerated Thu Mar 14 10:24:30 CET 2013
     */
    public ProcedureDefaults selectByPrimaryKey(String id) {
        ProcedureDefaults key = new ProcedureDefaults();
        key.setId(id);
        ProcedureDefaults record = (ProcedureDefaults) queryForObject("geowfst_procedure_defaults.ibatorgenerated_selectByPrimaryKey", key);
        return record;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table geowfst.procedure_defaults
     *
     * @ibatorgenerated Thu Mar 14 10:24:30 CET 2013
     */
    public void updateByPrimaryKeySelective(ProcedureDefaults record) {
        update("geowfst_procedure_defaults.ibatorgenerated_updateByPrimaryKeySelective", record);
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method corresponds to the database table geowfst.procedure_defaults
     *
     * @ibatorgenerated Thu Mar 14 10:24:30 CET 2013
     */
    public void updateByPrimaryKey(ProcedureDefaults record) {
        update("geowfst_procedure_defaults.ibatorgenerated_updateByPrimaryKey", record);
    }
}