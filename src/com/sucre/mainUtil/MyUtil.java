package com.sucre.mainUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import com.sucre.listUtil.MutiList;



/**
 * 常用工具�?.
 * 
 * @author sucre
 *
 */
public class MyUtil {
	// 定义各种List
	public static MutiList listId = new MutiList();
	public static MutiList listVid = new MutiList();
	public static MutiList listCookie = new MutiList();

	/**
	 * @param fileName 为当前目录下的文件名,带后�?
	 * @param list     为要加载进入的list对象
	 * @return 成功则返回list的size,不成功则返回错误信息.
	 */
	public static String loadList(String fName, MutiList list) {

		try {
			// 加载文件
			list.loadFromFile(fName);
			return ("导入成功<==>" + String.valueOf(list.getSize()));
		} catch (IOException e) {
			return ("导入错误：" + e.getMessage());
		}

	}

	/**
	 * aes加密方法,默认为AES/ECB/NoPadding,128位数据块,偏移量为�?.
	 * 
	 * @param keys 加密 的密�?
	 * @param data 要加密的数据
	 * @return 成功返回base64格式的字符串,不成功返回null!
	 */
	public static String aesEncrypt(String keys, String data) {
		String ret = null;
		try {
			// 创建cipher对象,这是�?个单例模�?.
			Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
			// 创建�?个key对象.
			SecretKeySpec key = new SecretKeySpec(keys.getBytes(), "AES");
			// 初始化为加密模式.
			c.init(Cipher.ENCRYPT_MODE, key);
			// 加密并返回byte[]
			byte[] result = c.doFinal(data.getBytes());
			// 对加密结果进行base64编码
			ret = new String(Base64.getEncoder().encode(result));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * AES解密算法.默认AES/ECB/NoPadding,128位数据块,偏移量为�?.
	 * 
	 * @param keys 加密时的密码
	 * @param data 要解密的数据
	 * @return 成功返回明文,不成功返回null
	 */
	public static String aesDecrypt(String keys, String data) {

		try {
			// 创建cipher对象,这是�?个单例模�?.
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			// 创建�?个key对象
			SecretKeySpec key = new SecretKeySpec(keys.getBytes(), "AES");
			// 使用密钥初始化，设置为解密模�?
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 执行操作,先对字符作base64解码,再解�?.
			byte[] result = cipher.doFinal(Base64.getDecoder().decode(data));
			// byte 转string 并返�?
			return (new String(result));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * 将字符串或文件压�?
	 * 
	 * @param data 为要压缩的字符串 @return,成功返回byte[]数组.
	 */
	public static byte[] gZip(String data) {
		// 用来接收压缩后的数据
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (
				// 创建压缩对象
				GZIPOutputStream g = new GZIPOutputStream(baos);

		) {
			// 压缩操作
			g.write(data.getBytes());
			g.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
    
	/**
	 * md5标准加密算法
	 * @param s 要加密的字符串
	 * @return
	 */
	public static String MD5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	private static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString().toLowerCase();
	}

	/**
	 * 生成指定范围内的随机�?,
	 * 
	 * @param u 范围上限
	 * @param l 范围下限
	 * @return String 类型的随机数�?
	 */
	public static String getRand(int u, int l) {
		Random r = new Random();
		return String.valueOf(((r.nextInt(u - l) + l)));
	}

	/**
	 * 随机生成�?串字母数字组合的字符�?
	 * 
	 * @param n 要生成字符的长度
	 * @return 长度为n的随机字符串
	 */
	public static String makeNonce(int n) {
		return makeSome(n, "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	/**
	 * 随机生成�?串数字组合的字符�?
	 * 
	 * @param n 要生成字符的长度
	 * @return 长度为n的随机字符串
	 */
	public static String makeNumber(int n) {
		return makeSome(n, "0123456789");
	}

	private static String makeSome(int n, String key) {
		String ret = "";
		if (n != 0) {

			for (int i = 1; i <= n; i++) {
				int p = Integer.parseInt(getRand(key.length(), 0));
				ret += key.charAt(p);
			}
		}
		return ret;
	}

	/**
	 * 取当前时�?.13�?
	 * 
	 * @return 当前时间毫秒
	 */
	public static String getTime() {
		long t = System.currentTimeMillis();
		return String.valueOf(t);
	}

	/**
	 * 取当前时�?.10�?
	 * 
	 * @return 当前时间毫秒
	 */
	public static String getTimeB() {
		long t = System.currentTimeMillis();
		return String.valueOf(t).substring(0, 10);
	}

	/**
	 * 取出�?有cookie数据
	 * 
	 * @param 包含�?有数据的http报头
	 * @return http报头的所有setcookie 数据
	 */
	public static String getAllCookie(String data) {
		String temp = "";
		int cookieIndex = 0;
		int endIndex = 0;
		String str2find = "Set-Cookie:";
		// 循环取出字符
		while ((cookieIndex = data.indexOf(str2find, cookieIndex)) != -1) {
			// 判断cookie是否符合规格!
			if ((endIndex = data.indexOf(";", cookieIndex)) != -1) {
				// 取出字符
				temp += data.substring(cookieIndex + str2find.length(), endIndex + 1);
				cookieIndex = endIndex;
			}
		}

		return temp;
	}

	/**
	 * 追加数据到文�?!文件默认位置为当前目�?
	 * 
	 * @param fileName 要保存的文件�?,请带上后�?
	 * @param data     要追加保存的数据,默认会自动加上换行符!.
	 */
	public static void outPutData(String fileName, String data) {
		if ("".equals(data)) {
			return;
		}
		// 定义文件
		File file = new File(fileName);
		try (
				// 创建文件�?
				OutputStream out = new FileOutputStream(file, true);) {
			// 写入文件!
			data += "\r\n";
			out.write(data.getBytes());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * 判断字符是否为空!
	 * @param data
	 * @return 为空返回true.
	 */
	public static boolean isEmpty(String data) {
    	return (data==null ||data.trim().length()==0)? true:false;
    }
	
	/**
	 * 判断list数组是否为空
	 * @param data list
	 * @return
	 */
	public static boolean isEmpty(List<?> list) {
    	return (list==null ||list.size()==0)? true:false;
    }
	/**
	  * 截取字符中所有的指定字符
	 * 
	 * @param start   要截取字符的�?始位�?
	 * @param ends    要截取字符的结束位置
	 * @param str2mid 源字符串
	 * @return 返回start 和ends �?包含位置的字符串,�?"|"分割!
	 */
	public static ArrayList<String> midWordAll(String start,String ends,String str2mid) {
		if(isEmpty(str2mid)) {return null;}
		//StringBuilder ret=new StringBuilder(str2mid.length());
		ArrayList<String> ret=new ArrayList<>();
		int i=0;
		String[] back=null;
		while((back=midWord(start,ends,str2mid,i))!=null) {
	    i=Integer.parseInt(back[1]);
		ret.add(back[0]);
	    }
		return ret;
	}
	/**
	 * 截取某一段字�?
	 * 
	 * @param start   要截取字符的�?始位�?
	 * @param ends    要截取字符的结束位置
	 * @param str2mid 源字符串
	 * @return 返回start 和ends �?包含位置的字符串
	 */
	public static String midWord(String start, String ends, String str2mid) {
		String[] ret=midWord(start,ends,str2mid,0);
		if(ret==null) {return null;}
		return ret[0];
	}
    /**
     * 截取某一段字�?
	 * 
	 * @param start   要截取字符的�?始位�?
	 * @param ends    要截取字符的结束位置
	 * @param str2mid 源字符串
	 * @param starts �?始查找的位置
	 * @return 返回start 和ends �?包含位置的字符串
     */
	public static String[] midWord(String start,String ends,String str2mid,int starts) {
		int begin, last;
		if ("".equals(str2mid)) {
			return null;
		}
		begin = str2mid.indexOf(start,starts);
		// 找到字符
		if (begin != -1) {
			last = str2mid.indexOf(ends, begin + start.length());
			if (last != -1) {
				String ret = str2mid.substring(begin + start.length(), last);
				starts=last;
				return new String[]{ret,String.valueOf(last)};
			}
		}
        return null;
    }
    
	/**
	 * 
	 * @param dataStr 要转换的字符例如\u64cd\u4f5c\u6210\u529f
	 * @return 返回明名
	 */
	public static String decodeUnicode(String dataStr) {

		final StringBuffer ret = new StringBuffer();
		if (!isEmpty(dataStr) && dataStr.indexOf("\\u") != -1) {
			String[] buffer = dataStr.split("(\\\\u)");
			String temp = buffer[0];
			ret.append(temp);
			for (int i = 1; i < buffer.length; i++) {
				temp = buffer[i];
				if (!isEmpty(temp)) {
					char letter;
					letter = (char) Integer.parseInt(temp.substring(0, 4), 16); // 16进制parse整形字符串�??
					ret.append(new Character(letter).toString());
					// 剩下的字符
					if (temp.length() > 4) {
						ret.append(temp.substring(4));
					}
				}
			}

		}
		return ret.toString();
	}

	/**
	 * 线程休眠.优雅�?�?,不用每次都try
	 */
	public static void sleeps(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			//e.printStackTrace();
			System.out.println("休眠出错：" + e.getMessage());
		}
	}
	
   
	/** 
        * 执行CMD命令,并返回String字符�? 
     */  
    public static String executeCmd(String strCmd)  {  
        Process p;
        StringBuilder sbCmd = new StringBuilder(); 
		try {
			p = Runtime.getRuntime().exec("cmd /c " + strCmd);
		
        
        BufferedReader br = new BufferedReader(new InputStreamReader(p  
                .getInputStream(),"GBK"));  
        String line;  
        while ((line = br.readLine()) != null) {  
            sbCmd.append(line + "\n");  
        }  
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("调用出错："+ e.getMessage());
		}  
        
        
        return sbCmd.toString();  
    }  
  
   /**
    *  建立 �?个宽带连�?	   
    * @param adslTitle 宽带连接的铝�?
    * @param adslName 宽带连接的账�?
    * @param adslPass 宽带连接的密�?
    * @return
    */
    public static String connAdsl(String adslTitle, String adslName, String adslPass)  {  
        System.out.println("正在建立连接.");  
        String adslCmd = "rasdial " + adslTitle + " " + adslName + " "  
                + adslPass;  
        String tempCmd = executeCmd(adslCmd);  
        // 判断是否连接成功  
        if (tempCmd.indexOf("已连�?") > 0) {  
            System.out.println("已成功建立连�?.");  
            
        } else {  
            System.err.println(tempCmd);  
            System.err.println("建立连接失败");  
            
        }  
        return tempCmd;  
    }  
  
    /**
     * 断开adsl拨号
     * @param adslTitle 宽带连接的名�?
     * @return
     */
    public static boolean cutAdsl(String adslTitle)  {  
        String cutAdsl = "rasdial " + adslTitle + " /disconnect";  
        String result = executeCmd(cutAdsl);  
         
        if (result.indexOf("没有连接")!=-1){  
        	System.out.println(adslTitle + "连接不存在?!");  
            return false;  
        } else {  
            System.out.println("连接已断开.");  
            return true;  
        }  
    }  
	
    /**
     * 导入宽带账号密码名称等信息
     * @param filename
     */
    public static void loadADSL(String filename,Info info) {
    	Properties properties =new Properties();
    	
    	try {
			properties.load(new FileInputStream(new File(filename)));
			info.setADSL(properties.getProperty("name"));
			info.setADSLName(properties.getProperty("id"));
			info.setADSLPass(properties.getProperty("pass"));
		} catch (FileNotFoundException e) {
			System.out.println("导入ip文件错误：" + e.getMessage());
		} catch (IOException e) {
			System.out.println("导入ip文件错误：" + e.getMessage());
		}
		
	}
}
