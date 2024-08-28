/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Volume;
import com.constant.ServerConstants;
import com.helper.AWSStatistics;
import com.helper.DBUtils;
import com.helper.DateHelper;
import com.helper.GraphModel;
import com.helper.InstanceUserModel;
import com.helper.SimpleCryptoAndroidJava;
import com.helper.StringHelper;
import com.helper.UserModel;

/**
 * 
 * @author Admin
 */
public class ConnectionManager extends DBUtils {
	public static HashMap clickStreamingMap = new HashMap();

	// static {
	// reloadClickStreaming();
	// }

	public static void reloadClickStreaming() {
		// clickStreamingMap = ConnectionManager
		// .getQueryMap("SELECT distinct  domain,cate FROM clickstreaming where length(coalesce(cate,''))>3");
	}

	public static Connection getDBConnection() {
		Connection conn = null;
		try {
			Class.forName(ServerConstants.db_driver);
			conn = DriverManager.getConnection(ServerConstants.db_url,
					ServerConstants.db_user, ServerConstants.db_pwd);
			System.out.println("Got Connection");
		} catch (SQLException ex) {
			ex.printStackTrace();
			// JOptionPane.showMessageDialog(
			// null,
			// "Please start the mysql Service from XAMPP Console.\n"
			// + ex.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

	public static UserModel checkLogin(HashMap parameters) {
		String username = StringHelper.n2s(parameters.get("username"));
		String pass = StringHelper.n2s(parameters.get("pass"));
		if (pass.length() > 0) {
			try {
				pass = SimpleCryptoAndroidJava.encryptString(pass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String query = "SELECT * FROM useraccounts where username like ? and pass = ?";
		UserModel um = null;
		List list = DBUtils.getBeanList(UserModel.class, query, username, pass);
		System.out.println("list size:" + list.size());
		if (list.size() > 0) {
			um = (UserModel) list.get(0);
		}
		return um;
	}

	public static List getAllUsers() {
		String query = "SELECT * FROM useraccounts ";
		UserModel um = null;
		List list = DBUtils.getBeanList(UserModel.class, query);
		System.out.println("list size:" + list.size());

		return list;
	}

	public static List getUserWiseNode(String uid) {
		String query = "select DISTINCT * from awsnode where userid = " + uid;
		UserModel um = null;
		List list = DBUtils.getBeanList(InstanceUserModel.class, query);
		System.out.println("list size:" + list.size());

		return list;

	}

	public static String insertUser(HashMap parameters) {
		System.out.println(parameters);
		String success = "";

		String fname = StringHelper.n2s(parameters.get("fname"));
		String lname = StringHelper.n2s(parameters.get("lname"));
		String phoneno = StringHelper.n2s(parameters.get("phoneno"));
		String userpass = StringHelper.n2s(parameters.get("userpass"));
		;
		String regions = StringHelper.n2s(parameters.get("regions"));
		String accessKey = StringHelper.n2s(parameters.get("accessKey"));
		String accessSecret = StringHelper.n2s(parameters.get("accessSecret"));
		
		
		;
		try {
			userpass = SimpleCryptoAndroidJava.encryptString(StringHelper
					.n2s(parameters.get("userpass")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String username = StringHelper.n2s(parameters.get("username"));

		String data = "Select 1 from useraccounts where phoneno like '"
				+ phoneno + "' OR username like '" + username + "'";
		boolean v = DBUtils.dataExists(data);
		if (!v) {
			String sql = "insert into useraccounts (fname, lname, phoneno, pass, username,region,accesskey, secretkey)  "
					+ "	values(?,?,?,?,?,?,?,?)";
			int list = DBUtils.executeUpdate(sql, fname, lname, phoneno,
					userpass, username, regions,accessKey,accessSecret);
			if (list > 0) {
				success = "User registered Successfully";
   
			} else {
				success = "Error adding user to database";
			}
		} else {
			success = "Duplicate user name or phone No";
		}
		return success;
	}

	public static void main(String[] args) {
		// getDBConnection();
		// getGraphData();
		// getEbsGraphData();
		// getPidata();
		// getDecisionData();
		// getUserWiseNode("1");
		// getDecisionData();
//		getVolumes("1");
		// System.out.println(l);
		// for (int i = 0; i < l.size(); i++) {
		// GraphModel gm = (GraphModel) l.get(i);
		// System.out.println(gm.getMaxDate());
		//
		// }
	}


	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static String method(String str) {
		if (str != null && str.length() > 0
				&& str.charAt(str.length() - 1) == 'x') {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

}
