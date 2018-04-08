package com.dcm.osgi.translateclient;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.dcm.osgi.translateclient.servlet.TranslateServiceServlet;

public class Activator implements BundleActivator, ServiceListener {

	private static BundleContext context;
	private ServiceReference serviceReference;
	private Servlet servlet;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		// 在TranslateServiceServlet中创建一个构造方法，将bundleContext传进去
		servlet = new TranslateServiceServlet(bundleContext);
		// 注册Servlet
		registerServlet();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		// 注销Servlet等资源
		unRegisterServlet();
		serviceReference = null;
		Activator.context = null;
	}

	@SuppressWarnings("unchecked")
	private void registerServlet() {
		if (null == serviceReference) {
			// 通过上下文获取服务对象的“引用”(需要通过MANIFEST.MF引入org.osgi.service.http包)
			serviceReference = context.getServiceReference(HttpService.class);
		}

		if (null != serviceReference) {
			// 得到http服务对象
			HttpService httpService = context.getService(serviceReference);
			if (httpService != null) {
				try {
					// 注册Servlet
					// 四个参数分别是:(映射地址,Servlet类本身,Dictionary,上下文)
					httpService.registerServlet("/servlet/translateServlet",servlet, null, null);
					// 注册静态资源(html等文件)位置
					// 四个参数分别是(访问地址,静态文件所在路径,上下文)
					httpService.registerResources("/page", "pages", null);
					System.out.println("翻译助手服务已启动成功，请通过/page/translate.html访问!");
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (NamespaceException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void unRegisterServlet() {
		if (null != serviceReference) {
			// 得到http服务对象
			HttpService httpService = context.getService(serviceReference);
			if (httpService != null) {
				try {
					// 注销Servlet
					// 四个参数分别是:映射地址
					httpService.unregister("/servlet/translateServlet");
					// 注销静态资源(html等文件)位置
					// 参数是访问地址
					httpService.unregister("/page");
					System.out.println("翻译助手服务已停用成功，谢谢使用！");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
			registerServlet();
			break;
		case ServiceEvent.UNREGISTERING:
			unRegisterServlet();
			break;
		default:
			break;
		}
	}
}