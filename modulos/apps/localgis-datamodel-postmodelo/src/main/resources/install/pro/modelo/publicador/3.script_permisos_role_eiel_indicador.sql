
--Borramos los permisos que tuviera el role
delete from r_group_perm where groupid=(select id from iusergrouphdr where name='EIEL_INDICADORES');
-- Borramos el rol

delete from iusergroupuser where groupid=(select groupid from iusergrouphdr where name='EIEL_INDICADORES');
delete from iusergrouphdr where name='EIEL_INDICADORES';

SELECT setval('public.seq_iusergrouphdr', (select max(id)::bigint from iusergrouphdr), true);
-- Creamos el rol de EIEL_INDICADOR
insert into iusergrouphdr(id,name,mgrid,type,remarks,crtrid,crtndate,updrid,upddate,id_entidad) 
values (nextval('seq_iusergrouphdr'),'EIEL_INDICADORES',nextval('seq_r_user_perm'),0,'Rol para Usuario Publicador EIEL',currval('seq_r_user_perm'),'2011-11-17',currval('seq_r_user_perm'),'2011-11-17',0);

-- Añadimos permisos al rol
/*DECLARE @LAYERS { @1}; -- Una columna por cada capa
SET @LAYERS[0][0]= 'Capas EIEL Indicadores';

--- PERMISOS

DECLARE @PERMISOS { @1,@2,@3,@4 }; -- Un columna por permiso
SET @PERMISOS[0][0]= 'Geopista.Layer.Escribir';
SET @PERMISOS[0][1]= 'Geopista.Layer.Leer';
SET @PERMISOS[0][2]= 'Geopista.Layer.ModificarSLD';
SET @PERMISOS[0][3]= 'Geopista.Layer.Añadir';

--PRINT COLUMNS(@PERMISOS);



SET @I = 0; -- @I is an integer
WHILE @I < COLUMNS(@LAYERS)
BEGIN
	set @LAYER= @LAYERS[0][@I];
	PRINT @LAYER;
	SET @IDACL = select idacl::integer from acl where name ='Capas EIEL Indicadores';
	SET @J = 0; -- @J is an integer
	WHILE @J < COLUMNS(@PERMISOS)
	BEGIN
	   set @PERM=@PERMISOS[0][@J];
	   SET @IDPERM = 
	   INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),@IDPERM,@IDACL);
	   SET @J = @J + 1;
	END
SET @I = @I + 1;
END
*/

INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Capas EIEL Indicadores') and def='Geopista.Layer.Escribir'),(select idacl::integer from acl where name ='Capas EIEL Indicadores'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl=(select idacl::integer from acl where name ='Capas EIEL Indicadores') and def='Geopista.Layer.Leer'),(select idacl::integer from acl where name ='Capas EIEL Indicadores'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Capas EIEL Indicadores') and def='Geopista.Layer.ModificarSLD'),(select idacl::integer from acl where name ='Capas EIEL Indicadores'));
INSERT INTO r_group_perm(groupid,idperm,idacl) VALUES (currval('seq_iusergrouphdr'),(select r_acl_perm.idperm::integer from r_acl_perm left join usrgrouperm on r_acl_perm.idperm=usrgrouperm.idperm where idacl= (select idacl::integer from acl where name ='Capas EIEL Indicadores') and def='Geopista.Layer.Añadir'),(select idacl::integer from acl where name ='Capas EIEL Indicadores'));

