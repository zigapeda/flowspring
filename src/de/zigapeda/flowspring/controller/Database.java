package de.zigapeda.flowspring.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import de.zigapeda.flowspring.Main;

public class Database {
	private Connection connection;
	
	public Database() {
		try {
			this.connection = DriverManager.getConnection("jdbc:hsqldb:file:" + Main.getAppdata() + "flowspring","flowspring","flowspring");
			Statement s = this.connection.createStatement();
			try {
				s.execute("select * from settings");
			} catch(SQLException e) {
				createDatabase();
			}
			s = this.connection.createStatement();
		} catch (SQLException e) {
			this.connection = null;
			e.printStackTrace();
		}
	}

    public Connection getConnection() {
        return this.connection;
    }
    
    private void createDatabase() {
    	try {
			Statement s = this.connection.createStatement();
			s.executeUpdate("create sequence int_gen start with 1;");
			s.executeUpdate("create sequence alb_gen start with 1;");
			s.executeUpdate("create sequence gre_gen start with 1;");
			s.executeUpdate("create sequence com_gen start with 1;");
			s.executeUpdate("create sequence ttl_gen start with 1;");
			s.executeUpdate("create table interprets (" +
					"  int_id int," +
					"  int_name varchar_ignorecase(256)," +
					"  int_cmpname varchar(256)," +
					"  constraint int_pk primary key (int_id)," +
					"  constraint int_uni_name unique (int_name)," +
					"  constraint int_uni_cmpname unique (int_cmpname)" +
					");");
			s.executeUpdate("create table albums (" +
					"  alb_id int," +
					"  alb_name varchar_ignorecase(256)," +
					"  alb_cmpname varchar(256)," +
					"  constraint alb_pk primary key (alb_id)," +
					"  constraint alb_uni_name unique (alb_name)," +
					"  constraint alb_uni_cmpname unique (alb_cmpname)" +
					");");
			s.executeUpdate("create table genres (" +
					"  gre_id int," +
					"  gre_name varchar_ignorecase(256)," +
					"  gre_cmpname varchar(256)," +
					"  constraint gre_pk primary key (gre_id)," +
					"  constraint gre_uni_name unique (gre_name)," +
					"  constraint gre_uni_cmpname unique (gre_cmpname)" +
					");");
			s.executeUpdate("create table comments (" +
					"  com_id int," +
					"  com_text varchar_ignorecase(4000)," +
					"  com_hash varchar(32)," +
					"  constraint com_pk primary key (com_id)," +
					"  constraint com_uni unique (com_hash)" +
					");");
			s.executeUpdate("create table titles (" +
					"  ttl_id int," +
					"  ttl_name varchar_ignorecase(256)," +
					"  ttl_cmpname varchar(256)," +
					"  ttl_int_id int," +
					"  ttl_alb_id int," +
					"  ttl_gre_id int," +
					"  ttl_com_id int," +
					"  ttl_track int," +
					"  ttl_year int," +
					"  ttl_duration int," +
					"  ttl_rating int," +
					"  ttl_playcount int," +
					"  ttl_path varchar(4096)," +
					"  constraint ttl_pk primary key (ttl_id)," +
					"  constraint ttl_fk_int foreign key (ttl_int_id) references interprets (int_id) on update cascade on delete cascade," +
					"  constraint ttl_fk_alb foreign key (ttl_alb_id) references albums (alb_id) on update cascade on delete cascade," +
					"  constraint ttl_fk_gre foreign key (ttl_gre_id) references genres (gre_id) on update cascade on delete cascade," +
					"  constraint ttl_fk_com foreign key (ttl_com_id) references comments (com_id) on update cascade on delete cascade," +
					"  constraint ttl_uni_name unique (ttl_int_id, ttl_alb_id,ttl_name)," +
					"  constraint ttl_uni_cmpname unique (ttl_int_id, ttl_alb_id,ttl_cmpname)," +
					"  constraint ttl_uni_path unique (ttl_path)" +
					");");
			s.executeUpdate("create table settings (" +
					"  set_name varchar(128)," +
					"  set_value varchar(512)," +
					"  constraint set_pk primary key (set_name)" +
					");");
			s.executeUpdate("create table dual (d varchar(1));");
			s.executeUpdate("insert into dual (d) values ('X');");
			s.executeUpdate("insert into settings (set_name) values ('readwindow.bounds');");
			s.executeUpdate("insert into settings (set_name) values ('window.bounds');");
			s.executeUpdate("insert into settings (set_name) values ('medialib.columns');");
			s.executeUpdate("create view soundtracks (stk_int_id " +
					"                        ,stk_alb_id " +
					"                        ,stk_ttl_id " +
					"                        ,stk_com_id " +
					"                        ,stk_gre_id " +
					"                        ,stk_interpret " +
					"                        ,stk_album " +
					"                        ,stk_title " +
					"                        ,stk_track " +
					"                        ,stk_year " +
					"                        ,stk_duration " +
					"                        ,stk_rating " +
					"                        ,stk_playcount " +
					"                        ,stk_comment " +
					"                        ,stk_genre " +
					"                        ,stk_path) " +
					"as " +
					"select int_id " +
					"      ,alb_id " +
					"      ,ttl_id " +
					"      ,com_id " +
					"      ,gre_id " +
					"      ,int_name " +
					"      ,alb_name " +
					"      ,ttl_name " +
					"      ,ttl_track " +
					"      ,ttl_year " +
					"      ,ttl_duration " +
					"      ,ttl_rating " +
					"      ,ttl_playcount " +
					"      ,com_text " +
					"      ,gre_name " +
					"      ,ttl_path " +
					"from titles " +
					"left outer join albums " +
					"             on ttl_alb_id = alb_id " +
					"left outer join interprets " +
					"             on ttl_int_id = int_id " +
					"left outer join comments " +
					"             on ttl_com_id = com_id " +
					"left outer join genres " +
					"             on ttl_gre_id = gre_id;");
			s.executeUpdate("create procedure insertTitle" +
					" (i_interpret varchar(256)," +
					"  i_intcmp varchar(256)," +
					"  i_album varchar(256)," +
					"  i_albcmp varchar(256)," +
					"  i_title varchar(256)," +
					"  i_ttlcmp varchar(256)," +
					"  i_comment varchar(4000)," +
					"  i_comhash varchar(32)," +
					"  i_genre varchar(256)," +
					"  i_grecmp varchar(256)," +
					"  i_track int," +
					"  i_year int," +
					"  i_duration int," +
					"  i_rating int," +
					"  i_playcount int," +
					"  i_path varchar(4096)) " +
					"modifies sql data " +
					"begin atomic" +
					"  declare intid int;" +
					"  declare albid int;" +
					"  declare comid int;" +
					"  declare greid int;" +
					"  declare ttlid int;" +
					"  declare status int;" +
					"  set status = 0;" +
					"  if(i_interpret is not null) then" +
					"    select int_id into intid from interprets where int_cmpname = i_intcmp;" +
					"    if(intid is null) then " +
					"      select next value for int_gen into intid from dual;" +
					"      insert into interprets (int_id, int_name, int_cmpname)" +
					"      values (intid, i_interpret, i_intcmp);" +
					"      set status = status + 1;" +
					"    end if;" +
					"  else" +
					"    set intid = null;" +
					"  end if;" +
					"  if(i_album is not null) then" +
					"    select alb_id into albid from albums where alb_cmpname = i_albcmp;" +
					"    if(albid is null) then" +
					"      select next value for alb_gen into albid from dual;" +
					"      insert into albums (alb_id, alb_name, alb_cmpname)" +
					"      values (albid, i_album, i_albcmp);" +
					"      set status = status + 2;" +
					"    end if;" +
					"  else" +
					"    set albid = null;" +
					"  end if;" +
					"  if(i_genre is not null) then" +
					"    select gre_id into greid from genres where gre_cmpname = i_grecmp;" +
					"    if(greid is null) then" +
					"      select next value for gre_gen into greid from dual;" +
					"      insert into genres (gre_id, gre_name, gre_cmpname)" +
					"      values (greid, i_genre, i_grecmp);" +
					"      set status = status + 4;" +
					"    end if;" +
					"  else" +
					"    set greid = null;" +
					"  end if;" +
					"  if(i_comment is not null) then" +
					"    select com_id into comid from comments where com_hash = i_comhash;" +
					"    if(comid is null) then" +
					"      select next value for com_gen into comid from dual;" +
					"      insert into comments (com_id, com_text, com_hash) " +
					"      values (comid, i_comment, i_comhash);" +
					"      set status = status + 8;" +
					"    end if;" +
					"  else " +
					"    set comid = null;" +
					"  end if;" +
					"  select ttl_id into ttlid from titles where ttl_cmpname = i_ttlcmp and ttl_alb_id = albid;" +
					"  if(ttlid is null) then" +
					"    insert into titles (ttl_id, ttl_name, ttl_cmpname, ttl_int_id, ttl_alb_id, ttl_gre_id, ttl_com_id," +
					"                    ttl_track, ttl_year, ttl_duration, ttl_rating, ttl_playcount, ttl_path)" +
					"    values (next value for ttl_gen, i_title, i_ttlcmp, intid, albid, greid, comid, i_track," +
					"                    i_year, i_duration, i_rating, i_playcount, i_path);" +
					"    set status = status + 16;" +
					"  else " +
					"    set status = status + 32;" +
					"  end if; " +
					"end;");
			s.executeUpdate("set ignorecase true;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
