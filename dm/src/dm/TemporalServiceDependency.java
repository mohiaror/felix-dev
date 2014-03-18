package dm;

/**
 * A Temporal Service dependency that can block the caller thread between service updates. Only useful for required stateless
 * dependencies that can be replaced transparently. A Dynamic Proxy is used to wrap the actual service dependency. When the
 * dependency goes away, an attempt is made to replace it with another one which satisfies the service dependency criteria. If
 * no service replacement is available, then any method invocation (through the dynamic proxy) will block during a configurable
 * timeout. On timeout, an unchecked <code>IllegalStateException</code> exception is raised (but the service is not
 * deactivated).
 * <p>
 * <b>This class only supports required dependencies, and temporal dependencies must be accessed outside the Activator (OSGi)
 * thread, because method invocations may block the caller thread when dependencies are not satisfied. </b>
 * <p>
 * Sample Code:
 * <p>
 * <blockquote>
 * 
 * <pre>
 * import org.apache.felix.dependencymanager.*;
 * 
 * public class Activator extends DependencyActivatorBase {
 *   public void init(BundleContext ctx, DependencyManager dm) throws Exception {
 *     dm.add(createService()
 *            .setImplementation(MyServer.class)
 *            .add(createTemporalServiceDependency()
 *                .setTimeout(15000)
 *                .setService(MyDependency.class)));
 *   }
 * 
 *   public void destroy(BundleContext ctx, DependencyManager dm) throws Exception {
 *   }
 * }
 * 
 * class MyServer implements Runnable {
 *   MyDependency _dependency; // Auto-Injected by reflection.
 *   void start() {
 *     (new Thread(this)).start();
 *   }
 *   
 *   public void run() {
 *     try {
 *       _dependency.doWork();
 *     } catch (IllegalStateException e) {
 *       t.printStackTrace();
 *     }
 *   }
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
public interface TemporalServiceDependency extends ServiceDependency {
    /**
     * Sets the timeout for this temporal dependency. Specifying a timeout value of zero means that there is no timeout period,
     * and an invocation on a missing service will fail immediately.
     * 
     * @param timeout the dependency timeout value greater or equals to 0
     * @throws IllegalArgumentException if the timeout is negative
     * @return this temporal dependency
     */
    TemporalServiceDependency setTimeout(long timeout);
}
