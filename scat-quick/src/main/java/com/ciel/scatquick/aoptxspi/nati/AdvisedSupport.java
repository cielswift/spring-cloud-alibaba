//package com.ciel.scatquick.aoptxspi.nati;
//
//import org.aopalliance.aop.Advice;
//import org.springframework.aop.Advisor;
//import org.springframework.aop.IntroductionAdvisor;
//import org.springframework.aop.TargetSource;
//import org.springframework.aop.framework.*;
//import org.springframework.aop.support.DefaultPointcutAdvisor;
//import org.springframework.aop.target.EmptyTargetSource;
//import org.springframework.aop.target.SingletonTargetSource;
//import org.springframework.util.ClassUtils;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//
//public class AdvisedSupport extends ProxyConfig implements Advised {
//
//////////////////////////////////////////////////////////////////////////////////////
/**
 * AdvisedSupport 继承了 ProxyConfig

 public class ProxyConfig implements Serializable {
 // 标记是否直接对目标类进行代理，而不是通过接口产生代理
 private boolean proxyTargetClass = false;

 // 标记是否对代理进行优化。启动优化通常意味着在代理对象被创建后，增强的修改将不会生效，因此默认值为false。
 // 如果exposeProxy设置为true，即使optimize为true也会被忽略。
 private boolean optimize = false;

 // 标记是否需要阻止通过该配置创建的代理对象转换为Advised类型，默认值为false，表示代理对象可以被转换为Advised类型
 boolean opaque = false;

 // 标记代理对象是否应该被aop框架通过AopContext以ThreadLocal的形式暴露出去。
 // 当一个代理对象需要调用它自己的另外一个代理方法时，这个属性将非常有用。默认是是false，以避免不必要的拦截。
 boolean exposeProxy = false;

 // 标记该配置是否需要被冻结，如果被冻结，将不可以修改增强的配置。
 // 当我们不希望调用方修改转换成Advised对象之后的代理对象时，这个配置将非常有用。
 private boolean frozen = false;
 }
 */
/////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * AdvisedSupport 实现 Advised

 public interface Advised extends TargetClassAware {

 * 返回配置是否已冻结，被冻结之后，无法修改已创建好的代理对象中的通知
 boolean isFrozen();

 * 是否对目标类直接创建代理，而不是对接口创建代理，通俗点讲：如果是通过cglib创建代理，此方法返回true，否则返回false
 boolean isProxyTargetClass();

 * 获取配置中需要代理的接口列表
 Class<?>[] getProxiedInterfaces();

 * 判断某个接口是否被代理
 boolean isInterfaceProxied(Class<?> intf);

 * 设置被代理的目标源，创建代理的时候，通常需要传入被代理的对象，最终被代理的对象会被包装为TargetSource类型的
 void setTargetSource(TargetSource targetSource);

 * 返回被代理的目标源
 TargetSource getTargetSource();

 * 设置是否需要将代理暴露在ThreadLocal中，这样可以在线程中获取到被代理对象，这个配置挺有用的，稍后会举例说明使用场景
 void setExposeProxy(boolean exposeProxy);

 * 返回exposeProxy
 boolean isExposeProxy();

 * 设置此代理配置是否经过预筛选，以便它只包含适用的顾问(匹配此代理的目标类)。
 * 默认设置是“假”。如果已经对advisor进行了预先筛选，则将其设置为“true”
 * 这意味着在为代理调用构建实际的advisor链时可以跳过ClassFilter检查。
 void setPreFiltered(boolean preFiltered);

 * 返回preFiltered
 boolean isPreFiltered();

 * 返回代理配置中干掉所有Advisor列表
 Advisor[] getAdvisors();

 * 添加一个Advisor
 void addAdvisor(Advisor advisor) throws AopConfigException;

 * 指定的位置添加一个Advisor
 void addAdvisor(int pos, Advisor advisor) throws AopConfigException;

 * 移除一个Advisor
 boolean removeAdvisor(Advisor advisor);

 * 移除指定位置的Advisor
 void removeAdvisor(int index) throws AopConfigException;

 * 查找某个Advisor的位置
 int indexOf(Advisor advisor);

 * 对advisor列表中的a替换为b
 boolean replaceAdvisor(Advisor a, Advisor b) throws AopConfigException;

 * 添加一个通知
 void addAdvice(Advice advice) throws AopConfigException;

 * 向指定的位置添加一个通知
 void addAdvice(int pos, Advice advice) throws AopConfigException;

 * 移除一个通知
 boolean removeAdvice(Advice advice);

 * 获取通知的位置
 int indexOf(Advice advice);

 * 将代理配置转换为字符串，这个方便排错和调试使用的
 String toProxyConfigString();
 }
 */
/////////////////////////////////////////////////////////////////////////////////////////////////////


//    public static final TargetSource EMPTY_TARGET_SOURCE = EmptyTargetSource.INSTANCE;
//
//    TargetSource targetSource = EMPTY_TARGET_SOURCE;
//
//    /** 建议器是否已经针对特定的目标类进行筛选 */
//    private boolean preFiltered = false;
//
//    /** 调用链工厂，用来获取目标方法的调用链 */
//    AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();
//
//    /** 方法调用链缓存：以方法为键，以顾问链表为值的缓存。 */
//    private transient Map<MethodCacheKey, List<Object>> methodCache;
//
//    //代理对象需要实现的接口列表。保存在列表中以保持注册的顺序，以创建具有指定接口顺序的JDK代理。
//    private List<Class<?>> interfaces = new ArrayList<>();
//
//    //配置的顾问列表。所有添加的Advise对象都会被包装为Advisor对象
//    private List<Advisor> advisors = new ArrayList<>();
//
//    //数组更新了对advisor列表的更改，这更容易在内部操作。
//    private Advisor[] advisorArray = new Advisor[0];
//
//
//    //无参构造方法
//    public AdvisedSupport() {
//        this.methodCache = new ConcurrentHashMap<>(32);
//    }
//
//    //有参构造方法，参数为：代理需要实现的接口列表
//    public AdvisedSupport(Class<?>... interfaces) {
//        this();
//        setInterfaces(interfaces);
//    }
//
//    //设置需要被代理的目标对象，目标对象会被包装为TargetSource格式的对象
//    public void setTarget(Object target) {
//        setTargetSource(new SingletonTargetSource(target));
//    }
//
//    //设置被代理的目标源
//    @Override
//    public void setTargetSource(@Nullable TargetSource targetSource) {
//        this.targetSource = (targetSource != null ? targetSource : EMPTY_TARGET_SOURCE);
//    }
//
//    //获取被代理的目标源
//    @Override
//    public TargetSource getTargetSource() {
//        return this.targetSource;
//    }
//
//    //设置被代理的目标类
//    public void setTargetClass(@Nullable Class<?> targetClass) {
//        this.targetSource = EmptyTargetSource.forClass(targetClass);
//    }
//
//    //获取被代理的目标类型
//    @Override
//    @Nullable
//    public Class<?> getTargetClass() {
//        return this.targetSource.getTargetClass();
//    }
//
//    /**
//     * 设置此代理配置是否经过预筛选，这个什么意思呢：通过目标方法调用代理的时候，
//     * 需要通过匹配的方式获取这个方法上的调用链列表，查找过程需要2个步骤：
//     * 第一步：类是否匹配，第二步：方法是否匹配，当这个属性为true的时候，会直接跳过第一步，这个懂了不
//     */
//    @Override
//    public void setPreFiltered(boolean preFiltered) {
//        this.preFiltered = preFiltered;
//    }
//
//    // 返回preFiltered
//    @Override
//    public boolean isPreFiltered() {
//        return this.preFiltered;
//    }
//
//    /**
//     * 设置顾问链工厂，当调用目标方法的时候，需要获取这个方法上匹配的Advisor列表，
//     * 获取目标方法上匹配的Advisor列表的功能就是AdvisorChainFactory来负责的
//     */
//    public void setAdvisorChainFactory(AdvisorChainFactory advisorChainFactory) {
//        Assert.notNull(advisorChainFactory, "AdvisorChainFactory must not be null");
//        this.advisorChainFactory = advisorChainFactory;
//    }
//
//    // 返回顾问链工厂对象
//    public AdvisorChainFactory getAdvisorChainFactory() {
//        return this.advisorChainFactory;
//    }
//
//
//    //设置代理对象需要实现的接口
//    public void setInterfaces(Class<?>... interfaces) {
//        Assert.notNull(interfaces, "Interfaces must not be null");
//        this.interfaces.clear();
//        for (Class<?> ifc : interfaces) {
//            addInterface(ifc);
//        }
//    }
//
//    //为代理对象添加需要实现的接口
//    public void addInterface(Class<?> intf) {
//        Assert.notNull(intf, "Interface must not be null");
//        if (!intf.isInterface()) {
//            throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
//        }
//        if (!this.interfaces.contains(intf)) {
//            this.interfaces.add(intf);
//            adviceChanged();
//        }
//    }
//
//    //移除代理对象需要实现的接口
//    public boolean removeInterface(Class<?> intf) {
//        return this.interfaces.remove(intf);
//    }
//
//    //获取代理对象需要实现的接口列表
//    @Override
//    public Class<?>[] getProxiedInterfaces() {
//        return ClassUtils.toClassArray(this.interfaces);
//    }
//
//    //判断代理对象是否需要实现某个接口
//    @Override
//    public boolean isInterfaceProxied(Class<?> intf) {
//        for (Class<?> proxyIntf : this.interfaces) {
//            if (intf.isAssignableFrom(proxyIntf)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    //获取配置的所有顾问列表
//    @Override
//    public final Advisor[] getAdvisors() {
//        return this.advisorArray;
//    }
//
//    //添加顾问
//    @Override
//    public void addAdvisor(Advisor advisor) {
//        int pos = this.advisors.size();
//        addAdvisor(pos, advisor);
//    }
//
//    //指定的位置添加顾问
//    @Override
//    public void addAdvisor(int pos, Advisor advisor) throws AopConfigException {
//        //这块先忽略，以后讲解
//        if (advisor instanceof IntroductionAdvisor) {
//            validateIntroductionAdvisor((IntroductionAdvisor) advisor);
//        }
//        addAdvisorInternal(pos, advisor);
//    }
//
//    //移除指定的顾问
//    @Override
//    public boolean removeAdvisor(Advisor advisor) {
//        int index = indexOf(advisor);
//        if (index == -1) {
//            return false;
//        }
//        else {
//            removeAdvisor(index);
//            return true;
//        }
//    }
//
//    //移除指定位置的顾问
//    @Override
//    public void removeAdvisor(int index) throws AopConfigException {
//        //当配置如果是冻结状态，是不允许对顾问进行修改的，否则会抛出异常
//        if (isFrozen()) {
//            throw new AopConfigException("Cannot remove Advisor: Configuration is frozen.");
//        }
//        if (index < 0 || index > this.advisors.size() - 1) {
//            throw new AopConfigException("Advisor index " + index + " is out of bounds: " +
//                    "This configuration only has " + this.advisors.size() + " advisors.");
//        }
//        //移除advisors中的顾问
//        Advisor advisor = this.advisors.remove(index);
//        if (advisor instanceof IntroductionAdvisor) {
//            IntroductionAdvisor ia = (IntroductionAdvisor) advisor;
//            // We need to remove introduction interfaces.
//            for (Class<?> ifc : ia.getInterfaces()) {
//                removeInterface(ifc);
//            }
//        }
//        //更新advisorArray
//        updateAdvisorArray();
//        //通知已改变，内部会清除方法调用链缓存信息。
//        adviceChanged();
//    }
//
//    @Override
//    public int indexOf(Advisor advisor) {
//        Assert.notNull(advisor, "Advisor must not be null");
//        return this.advisors.indexOf(advisor);
//    }
//
//    @Override
//    public boolean replaceAdvisor(Advisor a, Advisor b) throws AopConfigException {
//        Assert.notNull(a, "Advisor a must not be null");
//        Assert.notNull(b, "Advisor b must not be null");
//        int index = indexOf(a);
//        if (index == -1) {
//            return false;
//        }
//        removeAdvisor(index);
//        addAdvisor(index, b);
//        return true;
//    }
//
//    //批量添加顾问
//    public void addAdvisors(Advisor... advisors) {
//        addAdvisors(Arrays.asList(advisors));
//    }
//
//    //批量添加顾问
//    public void addAdvisors(Collection<Advisor> advisors) {
//        //配置如果是冻结状态，会抛出异常
//        if (isFrozen()) {
//            throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
//        }
//        if (!CollectionUtils.isEmpty(advisors)) {
//            for (Advisor advisor : advisors) {
//                if (advisor instanceof IntroductionAdvisor) {
//                    validateIntroductionAdvisor((IntroductionAdvisor) advisor);
//                }
//                Assert.notNull(advisor, "Advisor must not be null");
//                this.advisors.add(advisor);
//            }
//            updateAdvisorArray();
//            adviceChanged();
//        }
//    }
//
//    //此方法先忽略，用来为目标类引入接口的
//    private void validateIntroductionAdvisor(IntroductionAdvisor advisor) {
//        advisor.validateInterfaces();
//        // If the advisor passed validation, we can make the change.
//        Class<?>[] ifcs = advisor.getInterfaces();
//        for (Class<?> ifc : ifcs) {
//            addInterface(ifc);
//        }
//    }
//
//    //指定的位置添加顾问
//    private void addAdvisorInternal(int pos, Advisor advisor) throws AopConfigException {
//        Assert.notNull(advisor, "Advisor must not be null");
//        if (isFrozen()) {
//            throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
//        }
//        if (pos > this.advisors.size()) {
//            throw new IllegalArgumentException(
//                    "Illegal position " + pos + " in advisor list with size " + this.advisors.size());
//        }
//        this.advisors.add(pos, advisor);
//        updateAdvisorArray();
//        adviceChanged();
//    }
//
//    //将advisorArray和advisors保持一致
//    protected final void updateAdvisorArray() {
//        this.advisorArray = this.advisors.toArray(new Advisor[0]);
//    }
//
//    //获取顾问列表
//    protected final List<Advisor> getAdvisorsInternal() {
//        return this.advisors;
//    }
//
//    //添加通知
//    @Override
//    public void addAdvice(Advice advice) throws AopConfigException {
//        int pos = this.advisors.size();
//        addAdvice(pos, advice);
//    }
//
//    //指定的位置添加通知
//    @Override
//    public void addAdvice(int pos, Advice advice) throws AopConfigException {
//        //此处会将advice通知包装为DefaultPointcutAdvisor类型的Advisor
//        addAdvisor(pos, new DefaultPointcutAdvisor(advice));
//    }
//
//    //移除通知
//    @Override
//    public boolean removeAdvice(Advice advice) throws AopConfigException {
//        int index = indexOf(advice);
//        if (index == -1) {
//            return false;
//        }
//        else {
//            removeAdvisor(index);
//            return true;
//        }
//    }
//
//    //获取通知的位置
//    @Override
//    public int indexOf(Advice advice) {
//        Assert.notNull(advice, "Advice must not be null");
//        for (int i = 0; i < this.advisors.size(); i++) {
//            Advisor advisor = this.advisors.get(i);
//            if (advisor.getAdvice() == advice) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    //是否包含某个通知
//    public boolean adviceIncluded(@Nullable Advice advice) {
//        if (advice != null) {
//            for (Advisor advisor : this.advisors) {
//                if (advisor.getAdvice() == advice) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    //获取当前配置中某种类型通知的数量
//    public int countAdvicesOfType(@Nullable Class<?> adviceClass) {
//        int count = 0;
//        if (adviceClass != null) {
//            for (Advisor advisor : this.advisors) {
//                if (adviceClass.isInstance(advisor.getAdvice())) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//
//
//    //基于当前配置，获取给定方法的方法调用链列表（即org.aopalliance.intercept.MethodInterceptor对象列表）
//    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, @Nullable Class<?> targetClass) {
//        MethodCacheKey cacheKey = new MethodCacheKey(method);
//        //先从缓存中获取
//        List<Object> cached = this.methodCache.get(cacheKey);
//        //缓存中没有时，从advisorChainFactory中获取
//        if (cached == null) {
//            cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(
//                    this, method, targetClass);
//            this.methodCache.put(cacheKey, cached);
//        }
//        return cached;
//    }
//
//    //通知更改时调用，会清空当前方法调用链缓存
//    protected void adviceChanged() {
//        this.methodCache.clear();
//    }
//
//    //将other中的配置信息复制到当前对象中
//    protected void copyConfigurationFrom(AdvisedSupport other) {
//        copyConfigurationFrom(other, other.targetSource, new ArrayList<>(other.advisors));
//    }
//
//    //将other中的配置信息复制到当前对象中
//    protected void copyConfigurationFrom(AdvisedSupport other, TargetSource targetSource, List<Advisor> advisors) {
//        copyFrom(other);
//        this.targetSource = targetSource;
//        this.advisorChainFactory = other.advisorChainFactory;
//        this.interfaces = new ArrayList<>(other.interfaces);
//        for (Advisor advisor : advisors) {
//            if (advisor instanceof IntroductionAdvisor) {
//                validateIntroductionAdvisor((IntroductionAdvisor) advisor);
//            }
//            Assert.notNull(advisor, "Advisor must not be null");
//            this.advisors.add(advisor);
//        }
//        updateAdvisorArray();
//        adviceChanged();
//    }
//
//    //构建此AdvisedSupport的仅配置副本，替换TargetSource。
//    AdvisedSupport getConfigurationOnlyCopy() {
//        AdvisedSupport copy = new AdvisedSupport();
//        copy.copyFrom(this);
//        copy.targetSource = EmptyTargetSource.forClass(getTargetClass(), getTargetSource().isStatic());
//        copy.advisorChainFactory = this.advisorChainFactory;
//        copy.interfaces = this.interfaces;
//        copy.advisors = this.advisors;
//        copy.updateAdvisorArray();
//        return copy;
//    }
//}