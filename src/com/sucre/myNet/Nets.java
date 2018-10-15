package com.sucre.myNet;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.net.Socket;


import javax.net.SocketFactory; 

import javax.net.ssl.SSLSocketFactory;

/**
 * 负责socket发�?�与接收.接收后的数据交出调用者自己处�?.
 * 
 * @author sucre
 *
 */
public class Nets {

	/**
	 * https数据包发�?.get/post通用.
	 * 
	 * @param Host 服务器域�?
	 * @param Port 服务器端�?
	 * @param out  要发送的数据
	 * @return 成功后返回服务器返回的数�?,不成功返回错误码.
	 */
	public String goPost(String host, int port, byte[] data) {
		StringBuilder ret = new StringBuilder(data.length);
		// 创建sslsocket工厂
		SocketFactory factory = SSLSocketFactory.getDefault();
		try (
				// 括号内的对象自动释放.
				// 创建socker对象
				Socket sslsocket = factory.createSocket(host, port);
				// 创建要写入的数据对象,直接用bufferedoutputstream 更灵�?.必要时可传文件之�?.
				BufferedOutputStream out = new BufferedOutputStream(sslsocket.getOutputStream());

		) {
			// 写入要发送的数据并刷�?!
			out.write(data);
			out.flush();
			// 接收数据,为了解决乱码的情�?,要用inputstreamreader,用bufferedreader 包装后会更高效些.
			BufferedReader in = new BufferedReader(new InputStreamReader(sslsocket.getInputStream(), "UTF-8"));
			String str = null;
			char[] rev = new char[1024];
			int len = -1;
			while ((len = in.read(rev)) != -1) {
				ret.append(new String(rev, 0, len));
				// 由于socket会阻�?,当装不满缓冲区时,当作是结�?,
				if (len < 1024) {
					break;
				}
			}

			// 安全起见还是关闭�?下资�?.
			in.close();
			sslsocket.close();
			out.close();

		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println("网络错误：" + e.getMessage());
		}

		return ret.toString();
	}
    
	/**
	 * 普通数据包,非https
	 * @param host 
	 * @param port
	 * @param data
	 * @return
	 */
	public String GoHttp(String host, int port, byte[] data) {
		StringBuilder ret = new StringBuilder(data.length);
		try (
				// 括号内的对象自动释放.
				// 创建socker对象
				Socket socket = new Socket(host, port);
				// 创建要写入的数据对象,直接用bufferedoutputstream 更灵�?.必要时可传文件之�?.
				BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

		) {
			// 写入要发送的数据并刷�?!
			out.write(data);
			out.flush();
			// 接收数据,为了解决乱码的情�?,要用inputstreamreader,用bufferedreader 包装后会更高效些.
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			String str = null;
			char[] rev = new char[1024];
			int len = -1;
			while ((len = in.read(rev)) != -1) {
				ret.append(new String(rev, 0, len));
				// 由于socket会阻�?,当装不满缓冲区时,当作是结�?,
				if (len < 1024) {
					break;
				}
			}

			// 安全起见还是关闭�?下资�?.
			in.close();
			socket.close();
			out.close();

		} catch (Exception e) {
			System.err.println("http网络错误：" + e.getMessage());
		}

		return ret.toString();
	}

}
