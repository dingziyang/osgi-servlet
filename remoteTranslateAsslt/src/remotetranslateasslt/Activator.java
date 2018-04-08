package remotetranslateasslt;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.dcm.osgi.remotetranslateasslt.impls.TranslateServiceRemoteImpl;
import com.dcm.osgi.translate.service.TranslateService;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private ServiceRegistration<TranslateService> sr;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		// 注册Service服务
		sr = bundleContext.registerService(TranslateService.class,
				new TranslateServiceRemoteImpl(), null);
		System.out.println("remotetranslateasslt.Activator.start():远程查询服务已启动！");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		ServiceReference serviceRef = context.getServiceReference(TranslateService.class.getName());
		bundleContext.ungetService(serviceRef);
		System.out.println("remotetranslateasslt.Activator.stop():远程查询服务已停止！");
		Activator.context = null;
	}

}
