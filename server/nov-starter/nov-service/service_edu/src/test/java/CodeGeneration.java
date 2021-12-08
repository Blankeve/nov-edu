import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGeneration {
    public static void main(String[] args) {

//1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();

//2、全局配置
        GlobalConfig gc = new GlobalConfig();
//此处建议写项目/src/main/java源代码的绝对路径
        gc.setOutputDir("E:\\juam-workspace\\code\\git\\edu\\server\\nov-starter\\nov-service\\service_edu" + "/src/main/java");
//生成注释时的作者
        gc.setAuthor("juam");
//生成后是否打开资源管理器
        gc.setOpen(false);
        gc.setFileOverride(true);//重新生成时文件是否覆盖
        gc.setServiceName("%sService");//去掉Service接口的首字母I
        gc.setIdType(IdType.ID_WORKER_STR);//主键策略
        gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型
//如果开启Swagger,要引入相应的包
        gc.setSwagger2(true);//开启Swagger2模式

        mpg.setGlobalConfig(gc);

//3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://42.193.169.224:3306/nov_edu?useUnicode=true&characterEncoding=utf-8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("judb");
        dsc.setPassword("ju2009");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

//4、包配置
        PackageConfig pc = new PackageConfig();
//此处要注意：parent+moduleName为包的名字，在这个包下，创建对应的controller...
        pc.setParent("com.novedu.nov");
        pc.setModuleName("edu");//模块名
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

//5、策略配置
        StrategyConfig strategy = new StrategyConfig();
//数据库中表的名字，表示要对哪些表进行自动生成controllerservice、mapper...
        strategy.setInclude("edu_teacher");
//数据库表映射到实体的命名策略,驼峰命名法
        strategy.setNaming(NamingStrategy.underline_to_camel);
//生成实体时去掉表前缀，比如edu_course，如果不加下面这句，生成的实体类名字就是：EduCourse
//strategy.setTablePrefix("edu_");
//生成实体时去掉表前缀
//strategy.setTablePrefix(pc.getModuleName()+"_");

//数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);//lombok模型@Accessors(chain=true)setter链式操作

        strategy.setRestControllerStyle(true);//restfulapi风格控制器
        strategy.setControllerMappingHyphenStyle(true);//url中驼峰转连字符

        mpg.setStrategy(strategy);

//6、执行
        mpg.execute();
    }
}
