--example of postgres syntax
--CREATE TABLE weather (
--    city            varchar(80),
--    temp_lo         int,           -- low temperature
--    temp_hi         int,           -- high temperature
--   prcp            real,          -- precipitation
--    date            date
--);


--
-- Definition of table analysis
--

DROP TABLE IF EXISTS analysis;
CREATE TABLE analysis (
  id serial NOT NULL,
  analysis_id int NOT NULL default '0',
  analysis_name varchar(100)  NOT NULL default '',
  created_date date NOT NULL default '0001-01-01',
  created_by varchar(100) NOT NULL default '',
  active char(1) NOT NULL default 'T',
  removed char(1) NOT NULL default 'F',
  version int NOT NULL default '1',
  remark text NOT NULL, 
  PRIMARY KEY  (id)
-- KEY doesn't seem to work in postgresql
--  KEY analysis_id ("analysis_id")
);


--
-- Definition of table analysis_fields
--

DROP TABLE IF EXISTS analysis_fields;
CREATE TABLE analysis_fields (
  id serial NOT NULL,
  analysis_id int NOT NULL default '0',
  analysis_version int NOT NULL default '1',
  text_id varchar(100)  NOT NULL default '',
  result_min varchar(100)  default '',
  result_max varchar(100)  default '0',
  result_type varchar(100)  NOT NULL default 'text',
  unit varchar(25)  NOT NULL default '',
  reportable char(1)  NOT NULL default 'T',
  result_for_spec char(1)  NOT NULL default 'T',
  created_by varchar(100)  NOT NULL default '',
  created_date date NOT NULL default '0001-01-01',
  PRIMARY KEY  (id)
--  KEY analysis_id (analysis_id)
) ;



--
-- Definition of table analysis_history
--

DROP TABLE IF EXISTS analysis_history;
CREATE TABLE analysis_history (
  analysis_id int NOT NULL default '0',
  remark text  NOT NULL,
  analysis_version int default '0',
  analysis_fields text ,
  changed_date date NOT NULL default '0001-01-01',
  changed_by varchar(100)  NOT NULL default ''
-- need to check how timestamp is done in postgresql
--  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
) ;



--
-- Definition of table analysis_map
--

DROP TABLE IF EXISTS analysis_map;
CREATE TABLE analysis_map (
  id serial NOT NULL,
  map_name varchar(100)  NOT NULL default '',
  compound_id int NOT NULL default '0',
  created_date date NOT NULL default '0001-01-01',
  created_by varchar(100)  NOT NULL default '',
  changed_by varchar(100)  NOT NULL default '',
  changed_date date NOT NULL default '0001-01-01',
  remark text  NOT NULL,
  active char(1)  NOT NULL default 'F',
  version int NOT NULL default '0',
  PRIMARY KEY  (id)
) ;



--
-- Definition of table analysis_map_link
--

DROP TABLE IF EXISTS analysis_map_link;
CREATE TABLE analysis_map_link (
  map_id varchar(100)  NOT NULL default '',
  analysis_id varchar(100)  NOT NULL default '',
  PRIMARY KEY  (map_id,analysis_id)
) ;


--
-- Definition of table batch
--

DROP TABLE IF EXISTS batch;
CREATE TABLE batch (
  id serial NOT NULL,
  compound_id int default '0',
  text_id varchar(255)  default '',
  created_date date NOT NULL default '0001-01-01',
  created_by varchar(100)  NOT NULL default '',
  production_location varchar(255)  default '',
  notebook_reference varchar(255)  default '',
  description text ,
  locked char(1)  NOT NULL default 'F',
  purity varchar(100)  default '',
  PRIMARY KEY  (id)
--  KEY compound_id (compound_id)
) ;



--
-- Definition of table ci_configuration
--

DROP TABLE IF EXISTS ci_configuration;
CREATE TABLE ci_configuration (
  id serial NOT NULL,
  reg_key varchar(55) NOT NULL default '',
  reg_value varchar(255) NOT NULL default '',
  PRIMARY KEY  (id)
) ;

--
-- Dumping data for table ci_configuration
--

/*!40000 ALTER TABLE ci_configuration DISABLE KEYS */;
INSERT INTO ci_configuration (id,reg_key,reg_value) VALUES 
 (1,'isRegistered','0'),
 (3,'useCustomContainerId','0'),
 (4,'labelTemplate','container.lbl'),
 (5,'labelTemplatePath','C:\\CI Utility\\');
/*!40000 ALTER TABLE ci_configuration ENABLE KEYS */;


--
-- Definition of table compound
--

DROP TABLE IF EXISTS compound;
CREATE TABLE compound (
  id serial NOT NULL,
  cd_id int NOT NULL default '0',
  chemical_name varchar(255)  NOT NULL default '',
  cas_number varchar(100)  default '',
  beilstein_code varchar(100)  default '',
  density decimal(10,4) default '0.0000',
  remark text ,
  register_by varchar(100)  NOT NULL default '',
  register_date date NOT NULL default '0001-01-01',
  modified_by varchar(100)  NOT NULL default '',
--  modified_date timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (id)
--  KEY structure_index (cd_id)
) ;


--
-- Definition of table container
--

DROP TABLE IF EXISTS container;
CREATE TABLE container (
  id serial NOT NULL,
  compound_id int NOT NULL default '0',
  supplier_id int NOT NULL default '0',
  location_id int NOT NULL default '0',
  initial_quantity decimal(10,5) NOT NULL default '0.00000',
  current_quantity decimal(10,5) NOT NULL default '0.00000',
  empty char(1)  NOT NULL default 'F',
  unit varchar(10)  NOT NULL default '',
  register_by varchar(100)  NOT NULL default '',
  register_date date NOT NULL default '0001-01-01',
  owner varchar(100)  default '--',
  modified_by varchar(100)  default '',
--  modified_date timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  user_id int default '0',
  tara_weight decimal(10,2) NOT NULL default '0.00',
  remark text ,
  procurement_date date default NULL,
  expiry_date date default NULL,
  PRIMARY KEY  (id)
--  KEY com_index (compound_id)
) ;

--
-- Definition of table history
--

DROP TABLE IF EXISTS history;
CREATE TABLE history (
  id serial NOT NULL,
  tablename varchar(100)  NOT NULL default '',
  table_id int NOT NULL default '0',
  text_id varchar(255)  default '',
  change_details text ,
  text text  NOT NULL,
  changed_by varchar(100)  NOT NULL default '0',
  changed_date date NOT NULL default '0001-01-01',
  unit varchar(100)  default '',
  new_value varchar(100)  default '',
  old_value varchar(100)  default '',
  structure bytea,
--  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (id)
) ;


--
-- Definition of table jchemproperties
--

--chemaxon does support postgresql so should use their tool to create this table
-- mediumblob in mysql should be bytea in postgresql
DROP TABLE IF EXISTS jchemproperties;
CREATE TABLE jchemproperties (
  prop_name varchar(200) NOT NULL default '',
  prop_value varchar(200) default NULL,
  prop_value_ext bytea,
  PRIMARY KEY  (prop_name)
) ;

--
-- Dumping data for table jchemproperties
--

/*!40000 ALTER TABLE jchemproperties DISABLE KEYS */;
INSERT INTO jchemproperties (prop_name,prop_value,prop_value_ext) VALUES 
 ('db.autoIncrementPropertyName','AUTO_INCREMENT',NULL),
 ('db.ConstraintNeededForPrimaryKeyDef','false',NULL),
 ('db.existsBitwiseAND','true',NULL),
 ('db.isAutoIncrementProperty','true',NULL),
 ('registration.code','',NULL),
 ('registration.code.cartridge','0',NULL),
 ('registration.code.cluster','0',NULL),
 ('table.structures.absoluteStereo','true',NULL),
 ('table.structures.fingerprint.numberOfBits','512',NULL),
 ('table.structures.fingerprint.numberOfEdges','5',NULL),
 ('table.structures.fingerprint.numberOfOnes','2',NULL),
 ('table.structures.version','30',NULL),
 ('table.structures_new.absoluteStereo','true',NULL),
 ('table.structures_new.creationTime','2006-12-03 18:51:12.758',NULL),
 ('table.structures_new.fingerprint.numberOfBits','512',NULL),
 ('table.structures_new.fingerprint.numberOfEdges','6',NULL),
 ('table.structures_new.fingerprint.numberOfOnes','2',NULL),
 ('table.structures_new.fingerprint.numberOfStrucFPCols','0',NULL),
 ('table.structures_new.version','30',NULL);
INSERT INTO jchemproperties (prop_name,prop_value,prop_value_ext) VALUES 
 ('table.typ.absoluteStereo','true',NULL),
 ('table.typ.creationTime','2006-02-17 19:15:25.743',NULL),
 ('table.typ.fingerprint.numberOfBits','512',NULL),
 ('table.typ.fingerprint.numberOfEdges','6',NULL),
 ('table.typ.fingerprint.numberOfOnes','2',NULL),
 ('table.typ.version','19',NULL);
/*!40000 ALTER TABLE jchemproperties ENABLE KEYS */;


--
-- Definition of table location
--

DROP TABLE IF EXISTS location;
CREATE TABLE location (
  level int default '0',
  id serial NOT NULL,
  location_id int default '0',
  location_name varchar(100)  NOT NULL default '',
  PRIMARY KEY  (id)
) ;


--
-- Definition of table privileges
--

DROP TABLE IF EXISTS privileges;
CREATE TABLE privileges (
  name varchar(100)  NOT NULL default '',
  id serial NOT NULL,
  description varchar(255)  default NULL,
  order_by int NOT NULL default '0',
  display smallint NOT NULL default '1',
  PRIMARY KEY  (id)
) ;

--
-- Dumping data for table privileges
--

/*!40000 ALTER TABLE privileges DISABLE KEYS */;
INSERT INTO privileges (name,id,description,order_by,display) VALUES 
 ('adm',1,'Administrator',1,0),
 ('normal',2,'Normal Access (Check-in/out, information access etc)',1,1),
 ('sample_creator',3,'Create samples.',10,1),
 ('result_entry',4,'Perform result entry on created samples.',11,1),
 ('batch_manager',5,'Perform manager actions on batches.',12,1),
 ('batch_viewer',6,'View created batches.',12,1),
 ('report_adm',7,'Magage reports.',13,1),
 ('report_display',8,'View reports.',13,1),
 ('location_admin',9,'Manage locations.',4,1),
 ('compound_admin',10,'Manage compounds.',3,1),
 ('supplier_admin',11,'Manage suppliers,',7,1),
 ('container_admin',12,'Manage containers.',2,1),
 ('group_admin',13,'Manage groups.',8,1),
 ('unit_admin',14,'Manage units.',9,1),
 ('analysis_admin',15,'Manage analyis.',14,1),
 ('user_new',16,'Create new users.',5,1),
 ('user_remove',17,'Remove exsiting users.',5,1),
 ('user_resetpwd',18,'Reset password for existing users.',5,1),
 ('user_modify',19,'Modify existing users.',5,1),
 ('user_type_create',20,'Create user types.',6,1);
INSERT INTO privileges (name,id,description,order_by,display) VALUES 
 ('user_type_modify',21,'Modify user types.',6,1),
 ('sample_approver',22,'Access to approve samples.',10,1);
/*!40000 ALTER TABLE privileges ENABLE KEYS */;


--
-- Definition of table report_parameters
--

DROP TABLE IF EXISTS report_parameters;
CREATE TABLE report_parameters (
  report_id int NOT NULL default '0',
  parameter_name varchar(255)  NOT NULL default '',
  PRIMARY KEY  (report_id,parameter_name)
) ;

--
-- Dumping data for table report_parameters
--

/*!40000 ALTER TABLE report_parameters DISABLE KEYS */;
/*!40000 ALTER TABLE report_parameters ENABLE KEYS */;


--
-- Definition of table reports
--

DROP TABLE IF EXISTS reports;
CREATE TABLE reports (
  report_id serial NOT NULL,
  report_name varchar(150)  NOT NULL default '',
  display_name varchar(255)  NOT NULL default '',
  description varchar(255)  default '--',
  PRIMARY KEY  (report_id,report_name)
) ;

--
-- Dumping data for table reports
--

/*!40000 ALTER TABLE reports DISABLE KEYS */;
INSERT INTO reports (report_id,report_name,display_name,description) VALUES 
 (20,'userListJReport','List Of CI Users','This report creates a list of all the users,registered in the CI system.');
/*!40000 ALTER TABLE reports ENABLE KEYS */;


--
-- Definition of table resources
--

DROP TABLE IF EXISTS resources;
CREATE TABLE resources (
  text_id varchar(125)  NOT NULL default '',
  use_text varchar(100)  NOT NULL default '',
  mouse_text text ,
  alternative_text text ,
  icon varchar(100)  NOT NULL default '',
  icon_disabled varchar(100)  default '',
  visibility varchar(255)  default '',
  internal varchar(100)  NOT NULL default '',
  position int NOT NULL default '0',
  include_1 varchar(255)  default '',
  id_1 varchar(100)  default '',
  include_2 varchar(255)  default '',
  id_2 varchar(100)  default '',
  include_3 varchar(100)  default '',
  id_3 varchar(100)  default '',
  cas_required char(1)  default 'N',
  url varchar(255)  default '',
  resource varchar(255)  default '',
  sticky_text char(1)  default 'N',
  include_4 varchar(100)  default '',
  id_4 varchar(100)  default '',
  PRIMARY KEY  (text_id)
)  
-- max_rows is apparently not supported in postgresql 9.1
-- MAX_ROWS=4
;

--
-- Dumping data for table resources
--

/*!40000 ALTER TABLE resources DISABLE KEYS */;
INSERT INTO resources (text_id,use_text,mouse_text,alternative_text,icon,icon_disabled,visibility,internal,position,include_1,id_1,include_2,id_2,include_3,id_3,cas_required,url,resource,sticky_text,include_4,id_4) VALUES 
 ('KEMI_BRUG','Y','Search for data in the Kemi Brug system. Nu med ændring her...','The Kemi Brug search is not available for compounds without a CAS number, rember this please...','kemi_brug.gif','','normal','external',1,'!3!','kquery','!3!','kquery2','','','Y','http://www.kemibrug.dk/searchpage/search.tkl?entrytype=multibox&searchfield=TERMSCASNO&kquery1=&kquery3=&startmdate=&endmdate=','','Y',NULL,NULL),
 ('MSDS_FLINN','Y','Search msds for !1!','','msds_search.gif','','normal','external',5,'!1!','SEARCH_KEYWORD','AND','radiobutton','SEARCH_KEYWORD','action','N','http://www.flinnsci.com/search_MSDS.asp','','Y','TRUE','doSearch'),
 ('MSDS_SEARCH','Y','Search for MSDS information on the compound with CAS number: !3!. This search is perfomed at Sigma Aldrich MSDS database. If this link opens a new window containing a error message, go to the <a href=http://www.sigmaaldrich.com/catalog/search/SearchResultsPage target=blank>Sigma Aldrich</a> web page.','The search for MSDS is not available for compounds without a CAS number.','msds_search.gif','msds_search_disabled.gif','normal','external',2,'!3!','ProdCASNumber','','','','','Y','http://www.sigmaaldrich.com/catalog/search/SearchResultsPage','','Y',NULL,NULL),
 ('NEW_CONTAINER','Y','Register a new container of this compound.','You do not have access to registering new containers.','new_container.gif','nmr_search_disabled.gif','adm','internal',3,'','','compound_id','','','','','','newContainer_reg','N',NULL,NULL);
INSERT INTO resources (text_id,use_text,mouse_text,alternative_text,icon,icon_disabled,visibility,internal,position,include_1,id_1,include_2,id_2,include_3,id_3,cas_required,url,resource,sticky_text,include_4,id_4) VALUES 
 ('NMR_SEARCH','Y','Search for NMR data for the compound with CAS number: !3!.','The NMR search is not available for compounds without a CAS number','nmr_search.gif','','normal','external',4,'!3!','regno','','','','','Y','http://www.aist.go.jp/RIODB/SDBS/cgi-bin/direct_frame_top.cgi?lang=eng','','Y',NULL,NULL);
/*!40000 ALTER TABLE resources ENABLE KEYS */;


--
-- Definition of table result
--

DROP TABLE IF EXISTS result;
CREATE TABLE result (
  id serial NOT NULL,
  sample_id int NOT NULL default '0',
  analysis_id int NOT NULL default '0',
  analysis_field_id int NOT NULL default '0',
  entered_date date NOT NULL default '0001-01-01',
  entered_by varchar(100)  NOT NULL default '',
  calculated_value varchar(100)  default '',
  reported_value varchar(255)  NOT NULL default '',
  text_value varchar(255)  NOT NULL default '',
  numeric_value double precision NOT NULL default '0',
  status char(1)  NOT NULL default 'F',
  locked char(1)  NOT NULL default 'F',
  locked_by varchar(100)  default '',
  locked_date date default '0001-01-01',
  unit varchar(100)  NOT NULL default '',
  replicate_count int default '0',
  replicate_number int default '0',
  PRIMARY KEY  (id)
--  KEY sample_id (sample_id),
--  KEY analysis_id (analysis_id),
--  KEY analysis_field_id (analysis_field_id)
) ;


--
-- Definition of table result_history
--

DROP TABLE IF EXISTS result_history;
CREATE TABLE result_history (
  result_id int NOT NULL default '0',
  remark text  NOT NULL,
  changed_date date NOT NULL default '0001-01-01',
  changed_by varchar(100)  NOT NULL default '',
  value varchar(100)  NOT NULL default '--',
  unit varchar(100)  NOT NULL default ''
--  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
 -- KEY result_index (result_id)
) ;



--
-- Definition of table roles
--

DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
  user_name varchar(100)  NOT NULL default '',
  role varchar(100)  NOT NULL default '',
  type char(1)  default NULL,
  id serial NOT NULL,
  privileges_id int NOT NULL default '0',
  user_type_id int NOT NULL default '0',
  PRIMARY KEY  (id)
) ;

--
-- Dumping data for table roles
--

/*!40000 ALTER TABLE roles DISABLE KEYS */;
INSERT INTO roles (user_name,role,type,id,privileges_id,user_type_id) VALUES 
 ('ADMINISTRATOR','user_modify',NULL,204,19,1),
 ('ADMINISTRATOR','user_resetpwd',NULL,205,18,1),
 ('ADMINISTRATOR','user_remove',NULL,206,17,1),
 ('ADMINISTRATOR','user_new',NULL,207,16,1),
 ('ADMINISTRATOR','analysis_admin',NULL,208,15,1),
 ('ADMINISTRATOR','unit_admin',NULL,209,14,1),
 ('ADMINISTRATOR','group_admin',NULL,210,13,1),
 ('ADMINISTRATOR','container_admin',NULL,211,12,1),
 ('ADMINISTRATOR','supplier_admin',NULL,212,11,1),
 ('ADMINISTRATOR','compound_admin',NULL,213,10,1),
 ('ADMINISTRATOR','location_admin',NULL,214,9,1),
 ('ADMINISTRATOR','report_display',NULL,215,8,1),
 ('ADMINISTRATOR','report_adm',NULL,216,7,1),
 ('ADMINISTRATOR','batch_viewer',NULL,217,6,1),
 ('ADMINISTRATOR','batch_manager',NULL,218,5,1),
 ('ADMINISTRATOR','result_entry',NULL,219,4,1),
 ('ADMINISTRATOR','sample_creator',NULL,220,3,1),
 ('ADMINISTRATOR','adm',NULL,221,1,1),
 ('ADMINISTRATOR','sample_approver',NULL,222,22,1),
 ('ADMINISTRATOR','user_type_modify',NULL,223,21,1);
INSERT INTO roles (user_name,role,type,id,privileges_id,user_type_id) VALUES 
 ('ADMINISTRATOR','user_type_create',NULL,224,20,1);
/*!40000 ALTER TABLE roles ENABLE KEYS */;


--
-- Definition of table sample
--

DROP TABLE IF EXISTS sample;
CREATE TABLE sample (
  id serial NOT NULL,
  compound_id int NOT NULL default '0',
  remark text ,
  comment text ,
  batch int default '0',
  created_date date NOT NULL default '0001-01-01',
  created_by varchar(100)  NOT NULL default '',
  locked char(1)  NOT NULL default 'F',
  locked_by varchar(100)  default '--',
  analysis_map_id int NOT NULL default '0',
  locked_date date default NULL,
  container_id int NOT NULL default '0',
  PRIMARY KEY  (id)
--  KEY compound_id (compound_id),
--  KEY container_id (container_id)
) ;


--
-- Definition of table sample_analysis_link
--

DROP TABLE IF EXISTS sample_analysis_link;
CREATE TABLE sample_analysis_link (
  sample_id int NOT NULL default '0',
  analysis_id int NOT NULL default '0',
  analysis_version int NOT NULL default '0',
  PRIMARY KEY  (analysis_id,sample_id)
--  KEY analysis_id (analysis_id),
--  KEY sample_id (sample_id)
) ;


--
-- Definition of table sample_history
--

DROP TABLE IF EXISTS sample_history;
CREATE TABLE sample_history (
  sample_id int NOT NULL default '0',
  remark text  NOT NULL,
  changed_date date NOT NULL default '0001-01-01',
  changed_by varchar(100)  NOT NULL default '',
  batch int NOT NULL default '0',
  compound_id int NOT NULL default '0',
  change_remark text ,
--  timestamp timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  container_id int NOT NULL default '0'
--  KEY id_index (sample_id)
) ;


--
-- Definition of table structures
--

-- this table should also be created by the chemaxon tool to make sure its 
-- correct for postgresql

DROP TABLE IF EXISTS structures;
CREATE TABLE structures (
  cd_id serial NOT NULL,
  cd_structure bytea NOT NULL,
  cd_smiles varchar(254)  default NULL,
  cd_formula varchar(100)  default NULL,
  cd_molweight double precision default NULL,
  cd_fp1 int NOT NULL default '0',
  cd_fp2 int NOT NULL default '0',
  cd_fp3 int NOT NULL default '0',
  cd_fp4 int NOT NULL default '0',
  cd_fp5 int NOT NULL default '0',
  cd_fp6 int NOT NULL default '0',
  cd_fp7 int NOT NULL default '0',
  cd_fp8 int NOT NULL default '0',
  cd_fp9 int NOT NULL default '0',
  cd_fp10 int NOT NULL default '0',
  cd_fp11 int NOT NULL default '0',
  cd_fp12 int NOT NULL default '0',
  cd_fp13 int NOT NULL default '0',
  cd_fp14 int NOT NULL default '0',
  cd_fp15 int NOT NULL default '0',
  cd_fp16 int NOT NULL default '0',
  cas_name varchar(50)  default NULL,
  name varchar(100)  default NULL,
  cd_hash int NOT NULL default '0',
  cd_flags varchar(20)  default NULL,
--  cd_timestamp datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (cd_id)
--  KEY structures_hx (cd_hash)
) ;



--
-- Definition of table structures_ul
--

DROP TABLE IF EXISTS structures_ul;
CREATE TABLE structures_ul (
  update_id serial NOT NULL,
  update_info varchar(20)  NOT NULL,
  PRIMARY KEY  (update_id)
) ;

--
-- Dumping data for table structures_ul
--

/*!40000 ALTER TABLE structures_ul DISABLE KEYS */;
INSERT INTO structures_ul (update_id,update_info) VALUES 
 (1,'Updates'),
 (2,'Update:3468'),
 (3,'Update:3468'),
 (4,'Inserts'),
 (5,'Deletes'),
 (6,'Inserts'),
 (7,'Update:3461'),
 (8,'Update:3461'),
 (9,'Inserts');
/*!40000 ALTER TABLE structures_ul ENABLE KEYS */;


--
-- Definition of table supplier
--

DROP TABLE IF EXISTS supplier;
CREATE TABLE supplier (
  id serial NOT NULL,
  supplier_name varchar(100)  NOT NULL default '',
  PRIMARY KEY  (id)
) ;


--
-- Definition of table typ_ul
--

DROP TABLE IF EXISTS typ_ul;
CREATE TABLE typ_ul (
  update_id serial NOT NULL,
  update_info varchar(20)  NOT NULL,
  PRIMARY KEY  (update_id)
) ;

--
-- Dumping data for table typ_ul
--

/*!40000 ALTER TABLE typ_ul DISABLE KEYS */;
/*!40000 ALTER TABLE typ_ul ENABLE KEYS */;


--
-- Definition of table unit
--

DROP TABLE IF EXISTS unit;
CREATE TABLE unit (
  id serial NOT NULL,
  value varchar(25)  NOT NULL default '',
  PRIMARY KEY  (id)
--  UNIQUE KEY uniq_text_unit (value),
--  UNIQUE KEY value (value)
) ;

--
-- Dumping data for table unit
--

/*!40000 ALTER TABLE unit DISABLE KEYS */;
INSERT INTO unit (id,value) VALUES 
 (5,'%'),
 (8,'-'),
 (7,'dg C'),
 (1,'gram'),
 (12,'KpI'),
 (3,'Liter'),
 (2,'mg'),
 (4,'ml'),
 (6,'nm'),
 (10,'Sec.');
/*!40000 ALTER TABLE unit ENABLE KEYS */;


--
-- Definition of table users
--

-- user is a reserved term in postgresql so have changed to users - will have to change in the code
DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id serial NOT NULL,
  user_name varchar(15)  NOT NULL default '--',
  first_name varchar(25)  NOT NULL default '--',
  last_name varchar(45)  NOT NULL default '--',
  password varchar(255)  NOT NULL default '--',
  room_number varchar(100)  NOT NULL default '--',
  removed char(1)  NOT NULL default 'F',
  telephone varchar(20)  NOT NULL default '0',
  organisation varchar(255)  default '--',
  department varchar(255)  default '--',
  email varchar(255)  NOT NULL default '',
  user_type_id int NOT NULL default '0',
  PRIMARY KEY  (id)
) ;

--
-- Dumping data for table user
--

/*!40000 ALTER TABLE user DISABLE KEYS */;
INSERT INTO user (id,user_name,first_name,last_name,password,room_number,removed,telephone,organisation,department,email,user_type_id) VALUES 
 (96,'ADMINISTRATOR','ADMINISTRATOR','ADMINISTRATOR','eb0a191797624dd3a48fa681d3061212','','F','1','--','--','info@chemicalinventory.org',1);
/*!40000 ALTER TABLE user ENABLE KEYS */;


--
-- Definition of table user_group_container_link
--

DROP TABLE IF EXISTS user_group_container_link;
CREATE TABLE user_group_container_link (
  container_id int NOT NULL default '0',
  group_id int NOT NULL default '0',
  PRIMARY KEY  (group_id,container_id)
) ;


--
-- Definition of table user_group_location_link
--

DROP TABLE IF EXISTS user_group_location_link;
CREATE TABLE user_group_location_link (
  location_id int NOT NULL default '0',
  group_id int NOT NULL default '0',
  PRIMARY KEY  (location_id,group_id)
) ;


--
-- Definition of table user_group_user_link
--

DROP TABLE IF EXISTS user_group_user_link;
CREATE TABLE user_group_user_link (
  user_id int NOT NULL default '0',
  group_id int NOT NULL default '0',
  PRIMARY KEY  (user_id,group_id)
) ;


--
-- Definition of table user_groups
--

DROP TABLE IF EXISTS user_groups;
CREATE TABLE user_groups (
  id serial NOT NULL,
  name varchar(255)  NOT NULL default '--',
  PRIMARY KEY  (id)
--  UNIQUE KEY unique_name (name)
) ;

--
-- Definition of table user_types
--

DROP TABLE IF EXISTS user_types;
CREATE TABLE user_types (
  user_type_id serial NOT NULL,
  name varchar(100) NOT NULL default '',
  isAdministrator smallint NOT NULL default '0',
  PRIMARY KEY  (user_type_id)
) ;

--
-- Dumping data for table user_types
--

/*!40000 ALTER TABLE user_types DISABLE KEYS */;
INSERT INTO user_types (user_type_id,name,isAdministrator) VALUES 
 (1,'ADMINISTRATOR',1),
 (2,'NORMAL',0),
 (3,'SUPER USER',0);
/*!40000 ALTER TABLE user_types ENABLE KEYS */;


--
-- Definition of table user_types_privileges_link
--

DROP TABLE IF EXISTS user_types_privileges_link;
CREATE TABLE user_types_privileges_link (
  user_type_id int NOT NULL default '0',
  privileges_id int NOT NULL default '0'
--  KEY privileges_id (privileges_id),
--  KEY user_types_id (user_type_id)
) ;

--
-- Dumping data for table user_types_privileges_link
--

/*!40000 ALTER TABLE user_types_privileges_link DISABLE KEYS */;
INSERT INTO user_types_privileges_link (user_type_id,privileges_id) VALUES 
 (1,12),
 (1,10),
 (1,9),
 (1,16),
 (1,19),
 (1,17),
 (1,18),
 (1,20),
 (1,21),
 (1,11),
 (1,13),
 (1,14),
 (1,22),
 (1,3),
 (1,4),
 (1,5),
 (1,6),
 (1,7),
 (1,8),
 (1,15),
 (1,1),
 (2,2),
 (3,2),
 (3,12),
 (3,16),
 (3,18),
 (2,16),
 (2,19),
 (2,18),
 (3,8),
 (2,12),
 (2,8),
 (3,10),
 (3,19),
 (3,17),
 (3,20);
/*!40000 ALTER TABLE user_types_privileges_link ENABLE KEYS */;
