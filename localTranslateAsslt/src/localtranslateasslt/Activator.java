package localtranslateasslt;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.dcm.osgi.localtranslateasslt.impls.TranslateServiceLocalImpl;
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
				new TranslateServiceLocalImpl(), null);
		System.out.println("localtranslateasslt.Activator.start():本地查询服务已启动！");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		ServiceReference serviceRef = context
				.getServiceReference(TranslateService.class.getName());
		bundleContext.ungetService(serviceRef);
		System.out.println("localtranslateasslt.Activator.stop():本地查询服务已停止！");
		Activator.context = null;
	}

}
