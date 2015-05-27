package com.oso.appt.bean;

/**
 * 
 * @ClassName: User
 * @Description: 登录用户信息
 * @author yetl
 * @date 2015年5月21日
 *
 */
public class User implements java.io.Serializable{
	private static final long serialVersionUID = 6285703531138137448L;
	
	private String account;
	private String pwd;
	private String licence;
	private String remeber;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getLicence() {
		return licence;
	}
	public void setLicence(String licence) {
		this.licence = licence;
	}
	public String getRemeber() {
		return remeber;
	}
	public void setRemeber(String remeber) {
		this.remeber = remeber;
	}
	
	@Override
	public String toString(){
		return new StringBuffer().append("account:").append(account).append(",pwd:")
				.append(pwd).append(",licence:").append(licence).toString();
	}
}
