# Layui 踩坑合集


-- 开发环境配置 

    --vagrant 虚拟机管理（192.168.56.2）
        --安装centos
        --docker
        ----vagrant切换到root 密码默认是vagrant
        --安装并运行mysql 镜像
        ----root/root
        --安装redis







-- 1 表单的button 标签类型默认是submit，请求执行完会自动刷新页面

    --解决方法：
        1，将类型改为button
        2，执行完后请求，加上return false

--2 执行页面跳转

    -- 使用 top.location.href
        eg：top.location.href='pages/index';

--2022/7/18

    --1 修改密码
    --2 左边菜单栏
    ----2.1 新增收入页面  下拉支付方式

--2022/7/20

    --2 左边菜单栏
    ----2.2 收入列表（基本）

--2022/7/21

    --2 左边菜单栏
    ----2.2 收入列表（spring data jpa 实现按条件动态分页查询）
    ----有关spring data jpa的异常： Page 1 of 0 containing UNKNOWN instances的处理
        ----https://diuut.com/?p=1116
    ----2.3 收入列表，行内删除/更新数据（更新待办）

--2022/7/25

    --2 左边菜单栏
    ----2.3 实现行内数据更新（payment method 不能正常显示）
    ----2.3 月收入详情 正常显示数据 未排序

--2022/8/8
    
    --admin dashboard
    ----tree.render 提示 laydate.render is not a function 
        --原因： 版本冲突 
        --方案： 将layui版本提升到2.5.0以上版本可以解决

    ----角色赋权层级菜单
        -- 方案：权限实体嵌套

    ----前端层级菜单的渲染
        --laydate.render 的使用方法
    
--2022/8/11

    ----现象：非json参数请求，获取前端数据异常，提示数据转换异常
      --解决方案：
        --问题1：实体的属性名称不要与实体同名
                举个例子：
                    --Teacher 的属性不要有teacher（可坑死我了@_@!）
        --问题2：前端提交表单数据，表单子标签属性最好同时有 id 和 name 属性
                举个例子：
                    --<input type="text" id="money"name="money">

--2022/8/12

    ----现象：
        --解决：spring data jpa 分页查询没有数据 spring data jpa的分页插件index从0 开始，前端传入页码需要减1