# 动态定时任务

### 基本逻辑
* 新增任务 /start?taskNo=XXX&cron=xxx  
  将taskNo、cron内容存储到redis中
  * redis缓存一个list，用来存储当前的任务编号list，redis标识：task_*XXX*
  * redis缓存每个任务的执行规则，redis标识：task_cron_*taskNo*
* 停止任务 /stop?taskNo=XXX  
  结束任务taskNo
* 任务管理器
  * 配置服务启动自动执行任务 GetFormulaTask  
    GetFormulaTask实现自ApplicationRunner方法，ApplicationRunner接口可以让项目在启动时候初始化一些信息
    需要重写run()方法，这个run()方法会在SpringApplication.run(…)完成之前被调用   
    此时，任务管理器被调起
  * 在任务管理器中循环遍历当前任务列表
    * 任务列表为空，休息10s再执行
    * 任务列表不为空，则遍历任务编号  
      根据任务编号处理当前任务：
      * 当前任务已经存在则不处理  
      * 任务不存在则初始化，
        将任务按执行规则添加到定时任务中，
        在具体service中执行具体业务逻辑
        并将任务缓存到map  
