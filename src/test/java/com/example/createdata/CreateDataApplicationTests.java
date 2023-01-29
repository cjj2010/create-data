package com.example.createdata;

import com.apifan.common.random.source.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.createdata.mapper.TbUserInfoMapper;
import com.example.createdata.po.TbUserInfoPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class CreateDataApplicationTests {

    @Autowired
    private TbUserInfoMapper tbUserInfoMapper;


    @Test
    void contextLoads() {
    }

    @Test
    public void findAll() {
        List<TbUserInfoPO> teachers = tbUserInfoMapper.selectList(null);
//1.创建一个大小为10的线程池
        BlockingQueue queue = new LinkedBlockingQueue();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10L, TimeUnit.SECONDS, queue);
//给线程池添加任务
        for (int i = 0; i < 5001; i++) {
            TbUserInfoPO tbUserInfoPO = new TbUserInfoPO();
            tbUserInfoPO.setAppInfo(InternetSource.getInstance().randomAppName());
            tbUserInfoPO.setAddress(AreaSource.getInstance().randomAddress());
            tbUserInfoPO.setAddress1(AreaSource.getInstance().randomAddress());
            tbUserInfoPO.setCarNumber(PersonInfoSource.getInstance().randomFemaleIdCard(AreaSource.getInstance().randomProvince(), NumberSource.getInstance().randomInt(1, 101)));
            tbUserInfoPO.setCreateTime(new Date());
            tbUserInfoPO.setEmail(InternetSource.getInstance().randomEmail(10, "163.com"));
            tbUserInfoPO.setGender('m');
            tbUserInfoPO.setIpv4(InternetSource.getInstance().randomPublicIpv4());
            tbUserInfoPO.setUpdateTime(new Date());
            tbUserInfoPO.setMonetary(NumberSource.getInstance().randomPercent());
            tbUserInfoPO.setMonetary1(NumberSource.getInstance().randomPercent());
            tbUserInfoPO.setMonetary2(NumberSource.getInstance().randomPercent());
            tbUserInfoPO.setMonetary3(NumberSource.getInstance().randomPercent());
            tbUserInfoPO.setName(PersonInfoSource.getInstance().randomChineseName());
            tbUserInfoPO.setName1(PersonInfoSource.getInstance().randomEnglishName());
            tbUserInfoPO.setPassword(PersonInfoSource.getInstance().randomStrongPassword(16, false));
            tbUserInfoPO.setTelephone(PersonInfoSource.getInstance().randomChineseMobile());
            tbUserInfoPO.setTno(UUID.randomUUID().toString());
            tbUserInfoPO.setPortraitPath(OtherSource.getInstance().randomCompanyDepartment());
            tbUserInfoMapper.insert(tbUserInfoPO);
        }


        System.out.println("查询所有数据" + teachers);
    }


    @Test
    public void findAll1() throws ExecutionException, InterruptedException {
        //进行异步任务列表
        List<FutureTask<Integer>> futureTasks = new ArrayList<FutureTask<Integer>>();
        //线程池 初始化十个线程 和JDBC连接池是一个意思 实现重用
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        long start = System.currentTimeMillis();
        AtomicInteger atomicInteger = new AtomicInteger();
        //类似与run方法的实现 Callable是一个接口，在call中手写逻辑代码
        Callable<Integer> callable = new Callable<Integer>() {
            public Integer call() throws Exception {
                int i = atomicInteger.incrementAndGet();
                TbUserInfoPO tbUserInfoPO = new TbUserInfoPO();
                tbUserInfoPO.setAppInfo(InternetSource.getInstance().randomAppName());
                tbUserInfoPO.setAddress(AreaSource.getInstance().randomAddress());
                tbUserInfoPO.setAddress1(AreaSource.getInstance().randomAddress());
                tbUserInfoPO.setCarNumber(PersonInfoSource.getInstance().randomFemaleIdCard(AreaSource.getInstance().randomProvince(), NumberSource.getInstance().randomInt(1, 101)));
                tbUserInfoPO.setCreateTime(new Date());
                tbUserInfoPO.setEmail(InternetSource.getInstance().randomEmail(10, "163.com"));
                tbUserInfoPO.setGender('m');
                tbUserInfoPO.setIpv4(InternetSource.getInstance().randomPublicIpv4());
                tbUserInfoPO.setUpdateTime(new Date());
                tbUserInfoPO.setMonetary(NumberSource.getInstance().randomPercent());
                tbUserInfoPO.setMonetary1(NumberSource.getInstance().randomPercent());
                tbUserInfoPO.setMonetary2(NumberSource.getInstance().randomPercent());
                tbUserInfoPO.setMonetary3(NumberSource.getInstance().randomPercent());
                tbUserInfoPO.setName(PersonInfoSource.getInstance().randomChineseName());
                tbUserInfoPO.setName1(PersonInfoSource.getInstance().randomEnglishName());
                tbUserInfoPO.setPassword(PersonInfoSource.getInstance().randomStrongPassword(16, false));
                tbUserInfoPO.setTelephone(PersonInfoSource.getInstance().randomChineseMobile());
                tbUserInfoPO.setTno(UUID.randomUUID().toString());
                tbUserInfoPO.setPortraitPath(OtherSource.getInstance().randomCompanyDepartment());
                tbUserInfoMapper.insert(tbUserInfoPO);
                System.out.println("任务执行:获取到结果 :" + i);
                return i;
            }
        };

        for (int i = 0; i < 500000; i++) {
            //创建一个异步任务
            FutureTask<Integer> futureTask = new FutureTask<Integer>(callable);
            futureTasks.add(futureTask);
            //提交异步任务到线程池，让线程池管理任务 特爽把。
            //由于是异步并行任务，所以这里并不会阻塞
            executorService.submit(futureTask);
        }

        int count = 0;
        for (FutureTask<Integer> futureTask : futureTasks) {
            //futureTask.get() 得到我们想要的结果
            //该方法有一个重载get(long timeout, TimeUnit unit) 第一个参数为最大等待时间，第二个为时间的单位
            count += futureTask.get();
        }
        long end = System.currentTimeMillis();
        System.out.println("线程池的任务全部完成:结果为:" + count + "，main线程关闭，进行线程的清理");
        System.out.println("使用时间：" + (end - start) + "ms");
        //清理线程池
        executorService.shutdown();
    }


}
