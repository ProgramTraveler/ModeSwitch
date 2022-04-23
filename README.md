# ---

## 更新日志

### 2021-12-31

#### 环境配置

* 配置Android环境，本来是想用`IDEA`做的，但是最新的`IDEA`好像不太配套，在官网上好像`Android`的`AVD`在2010就没有再更新了
* 所以选择的编译环境是 `Android studio`
* 配置`Android studio`的环境倒是没什么，只要别放**C盘**就行
* 唯一要注意的就是 `AVD`的安装位置是没有选择的，只能再安装完成之后进行修改，一般默认是在 Android Studio默认的AVD路径是在**C:\Users\用户名.android\avd**下
* **用户名不能是中文，否则运行虚拟机的时候会报 The emulator process for AVD Nexus_4_API_22 has terminated.的错误**

---

### 2022-01-01

#### 前期准备

* 创建仓库
* 了解Android知识

---

### 2022-01-04

#### 开始项目的开发工作

* 完成Android的基础理论的学习
* 了解项目的开发流程，准备开始对项目进行代码实现
* 先对用户界面进行编写，后续完成操作界面的实现z
* 初步确定进行单手主辅的程序编写

---

### 2022-01-05

#### 单手主辅模式的开发

##### 完成的工作

* 暂时先将单手辅助模式作为主活动进行测试，其实主活动应该是启动界面，这个后续再调整
* 测试`Android`的屏幕坐标系，检查获取的屏幕坐标是否正确
* 在实机上进行测试，主要是测试坐标位置是否合理
* 开始进行触屏操作的代码编写
* 对三个环的位置进行调整，在屏幕上显示合理
* 对是否闭环的条件要重新考虑，如果只是单纯的考虑点之间的坐标问题，难免会出现巧合，因为刚出发的时候也是误差最小的时候，这个**整体的逻辑得好好考虑一下**

##### 开发问题

* 完成单手主辅绘制界面的编写，解决程序在AVD上 **keeping stop**的错误
* 在终端上无法显示输出值 **--->** 这里要么使用 ` System.out.println()` 或者使用`loge()` 在 **logcat** 中查看，总算解决这一问题
* 画线界面和图形显示界面存在冲突，图形显示界面会将画线界面覆盖 **--->** 原因是 **paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));**，这个会导致取两层绘制交集，显示上层，所以线条不会显示，直接注释掉就好了

---

### 2022-01-09

#### 单手主辅模式下对画线闭环的判断逻辑

##### 完成的工作

* 确定圆环与屏幕是在同一坐标系，检查数学逻辑
* 解决对圆环的判定问题
* 优化画线的精确度，解决断触的问题
* 实现三个圆环依次出现
* 开始着手实现菜单的设计，估计要对Android的多点触摸机制进行了解

###### 圆环判定的逻辑

* 首先会记录一下画线的初始位置
* 由于选择的是与出初始点的相对位置作为是否是闭环的判断条件，而最初画线的时候肯定是满足条件的（**是一个由小到大的过程**）
* 所以附加了一个判断条件 **--->** 画过半圆

##### 开发问题

* 把画圆的坐标换为参数后无法显示的问题 **--->** 在获取到屏幕的大小后进行赋值
* 第三个环在闭环后不显示的问题 **--->** 只考虑到`x`轴的差值问题，没有考虑到`y`轴的差值，考虑到可能会在不同位置开始，将判断条件改为求两点之间的距离，而不单纯的只以坐标为判断依据

---

### 2022-01-10

#### 单手主辅模式下菜单的响应及显示

##### 完成工作

* 实现双指触发菜单（多指触发的机制）
* 实现对双指的监听

```

* 当我们一个手指触摸屏幕 ——> 触发ACTION_DOWN事件
* 接着有另一个手指也触摸屏幕 ——> 触发ACTION_POINTER_DOWN事件,如果还* 有其他手指触摸，继续触发
* 有一个手指离开屏幕 ——> 触发ACTION_POINTER_UP事件，继续有手指离开，继续触发
* 当最后一个手指离开屏幕 ——> 触发ACTION_UP事件
* 而且在整个过程中，ACTION_MOVE事件会一直不停地被触发

```

##### 开发问题

* 在双指进行操作时，第一根手指的`id为0`，再按下第二根手指时`id为1`，而当释放第一根手指，再按下时，会出现直接将两个点连成一条线 **--->** 因为第一根手指释放再按下`id还是0`，还是会触发画线的函数，所以对当前屏幕的手指数也作为判断条件

* 当第二根手指进入时，会出现`Move`事件并没有响应 **--->** 因为后台还一直在处理第一根手指的事件序列

---

### 2022-01-11

#### 完成工作

* 尝试实现对第二根手指的检测，能够检测到该手指的移动，这是实现菜单选择的关键


#### 开发问题

* 第二根手指的移动始终不能获得

---

### 2022-01-13

#### 完成工作

* 继续测试第二根手指的焦点，这个困扰属实是有点久，还是没有什么好多解决办法
* 实现菜单触发。开始编写菜单界面（一级展示菜单）
* 对一级菜单进行划分，完成菜单提示文字的位置排版
* 完成一级菜单的选择高亮显示，主要确定可以实现的范围

#### 开发问题

* 对第二根手指的识别问题 **--->** 突然想到可以直接获得这第二根手指的位置，因为两根手指在移动的时候，虽然焦点一直在第一根手指上，但是做菜单触发的时候它还是会移动，所以可以通过这个现象来实时更新第二根手指的位置，实现菜单触发，问题总算解决了
* 一级菜单的显示问题

---

### 2022-01-14

#### 完成工作

* 解决一级菜单的显示问题
* 实现菜单根据选择进行高亮，主要还是注意每一个画布之间的逻辑关系，其实一级菜单不用展开，但这个功能用来测试也不错，反正二级菜单也要用到高亮
* 正常的应该是一级菜单打开二级菜单，然后二级菜单根据手指区域高亮
* 解决异常退出的问题
* 一级菜单与二级菜单兼容
* 二级菜单的一些坐标位置还有待计算

#### 开发问题

* 绘制一次后异常退出 **--->** 数组下标越界

---

### 2022-01-17

#### 完成工作

* 建立二级菜单高亮的坐标
* 完成二级颜色菜单的高亮显示
* 开始实现颜色菜单的选择

#### 开发问题

* 二级菜单高亮显示位置不一致 **--->** 坐标系建立错误
* 选择颜色后，所有的线都成为切换后的颜色 **--->** 这个就是每次画线的时候直接将之前存下的点，按当前颜色重新再绘制

---

### 2022-01-18

#### 完成工作

* 解决选择颜色后会将之前的线条一起覆盖的问题，对于这个问题还是采用之前项目的方法，但在这里应该是设置一个`Path`类
* 顺利完成颜色选择问题
* 开始编写像素的二级菜单展开问题以及选择显示问题
* 完成像素二级菜单的区域划分

#### 开发问题

* 当二级菜单未展开，但在满足二级菜单选择的范围内，依然还是会选择颜色 **--->** 附加一个判断

---

### 2022-01-19

#### 完成工作

* 像素二级菜单的展示和选择
* 解决画笔颜色不一致问题
* 暂时先完成这些，后面应该还是会有一些调整，等待后续调制
* 确定动态

#### 开发问题

* 菜单之间的选择和切换还是存在逻辑问题，偶尔会出现令人意外的效果
* 颜色切换和像素切换不同步 **--->** 记录下当前的颜色和像素，在合适的位置给下一个路径进行赋值

---

### 2022-01-23

#### 完成工作

* 因为每个模式都会用到环的显示所以将环的显示独立出来

#### 开发问题

* 环的显示出现问题，无法正常实现逻辑功能 **--->** 对逻辑进行层层测试，原因是对在圆内的逻辑判断没有正常执行，主要还是参数错误

---

### 2022-01-24

#### 完成工作

* 后续还有两种以上的模式，所以再添加一个菜单选择的主页面
* 设置 `Intent` 实现两个活动之间的穿梭 
* 设置每个按钮的位置，解决覆盖问题
* 初始界面现在只是简单设置一下，暂时以实现功能为主，后续时间允许的条件下可以进行微调
* 设置每个活动的标题

#### 开发问题

* 按钮出现覆盖问题，且修改坐标无法生效
* 标题覆盖问题

---

### 2022-02-03

#### 完成工作

* 完成单手动态模式的环显示

#### 开发问题

* **View**的分别显示问题 **--->** 在最初的响应界面无法按菜单按钮进行正确响应，这个问题还是有点困惑 

---

### 2022-02-04

#### 完成工作

* 继续解决 **View** 响应问题，这个问题依旧还是关键要克服的技术问题
* **View** 界面的响应问题需要在 **layout** 中设置所需要使用的具体 **View** ，如果没有设置，会产生一个默认的界面，无法进行自定义
* 对单手主辅模式的菜单位置进行调整
* 开始编写单手动态模式界面，这个界面和单手主辅的模式的区别就是菜单的选择和显示，该模式下的菜单位置是固定的
* 设计一级菜单的显示，一级菜单依旧分为颜色一级菜单和像素一级菜单

#### 开发问题

* 

---

### 2022-02-05

#### 完成工作

* 对整体菜单的响应逻辑进行探究
* 二级菜单的设置以及显示问题，先进行显示测试，后续进行触发安排
* 设置二级菜单的位置和排版，二级菜单的编写完成
* 本来想编写二级菜单的高亮显示，菜单的响应过程比较复杂，放到后面再去编写，但是发现二级菜单的高亮显示需要在菜单响应后才能进行测试，所以还是得先写菜单的响应，这个过程可能很复杂，估计会花掉大部分时间


#### 开发问题

* 菜单的响应是一个复杂的过程，因为这包含了两个手指的独立操作，每一个手指都能控制菜单
* 第二根手指控制的一级菜单跳转无法正常执行 **--->** 逻辑错误

---

### 2022-02-07

#### 完成工作

* 继续完成单手动态模式的菜单切换功能
* 完成菜单的对手指位置的检测
* 开始编写二级菜单的选择高亮
* 完成抬手对菜单的选中和切换，并能完成颜色和像素的赋值

#### 开发问题

* 颜色二级菜单不能正常显示 **--->** 注意画笔清除和绘制的顺序，应该是先清除再绘制，不要颠倒顺序
* 二级菜单在抬手后不能正常收回 **--->** 之前记录的手指位置信息没有清除，导致每次检测都能通过
* 二级菜单高亮异常 **--->** 注意每个组件的产生顺序以及撤销的过程
* 二级菜单移动选择时多处高亮，主要问题还是出现在像素的切换上面 **--->** 每次绘制都将二级菜单再绘制一次（其实也就是将以前的覆盖一下），不这样也行，这只是一个特定的触发效果

---

### 2022-03-12

#### 完成工作

* 对已经开发的项目进行测试
* 对需要调整的内容进行总结
```cpp

单手主辅模式：
（1）高亮区域由加粗显示改为扇形显示，颜色为绿色，扇形区域占整体部分的一半
（2）调整呼出菜单的大小，目前测试结果发现菜单有点大
（3）调整菜单的提示字体大小

---

区域映射模式（原单手动态模式）：
（1）将菜单调整到左上角显示（原来的位置是在中间位置）
（2）当进行菜单选择，设置一个矩形提示光标
（3）菜单选择提示栏高亮显示为绿色
（4）调整菜单栏的粗细
（5）调整手指移动的距离和目标移动距离比

---

通用修改：
（1）加入切换提示功能，提示用户目标颜色和目标像素
（2）在界面右小角增加一个返回主页面的按钮
（3）暂时确定需要记录的数据（这个先放在最后写）

```
* **对于位置和大小问题需要在大屏上进行修改，所以相关调整放在最后进行**

###### 单手主辅模式
* 完成菜单的高亮显示
* 完成菜单大小的调整 **--->** 调整为原来的`2 / 3`

###### 区域映射模式
* 对菜单位置以及菜单像素的大小进行调整
* 修改菜单高亮的显示效果

##### 开发问题

###### 单手主辅模式
* 高亮位置异常  **--->** 坐标设置错误
###### 区域映射模式
* 菜单高亮没有对一级颜色和像素菜单进行覆盖，猜测是绘制优先级的问题

---

### 2022-03-13

#### 完成工作

* 在区域映射模式中添加矩形提示光标
* 将映射比例设为`1：3`，具体还得以测试为准、
* 添加返回按钮，暂时将按钮位置放在右上角，并添加按钮监听

#### 开发问题

* 没有双击也能触发菜单切换问题 **--->** 添加一个对双击状态的检测

---

### 2022-03-14

#### 完成工作

* 在测试界面添加切换提示界面
* 完成提示文字部署
* 将部署到相关坐标放在一个类中，方便后续的坐标调整
* 设置随机类，将不同颜色和像素组合出现

#### 开发问题

* 切换成变量后出现部分位置错位的情况 **--->** 设置的比例错误

---

### 2022-03-15

#### 完成工作

* 对测试获得的数据进行导出测试
* `android`的文件存储与之前写过的代码还是有差异，这个估计会花费比我预想还要长的时间
* 文件保存功能完成，后面继续提示模块的开发工作

#### 开发问题

* 文件无法实现存储 **--->** 需要写入完整路径，在代码中加入`app`的存储权限，并在设备上开启权限

---

### 2022-03-16

#### 完成工作

* 实现切换提示信息的随机出现
* 完成单手主辅模式中的像素和颜色提示模块
* 实现当前颜色随着菜单切换进行相应的转换
* 对区域映射技术进行同样的功能部署
* 调整区域映射模式的一级菜单位置（菜单长度进行下调）

#### 开发问题

* 颜色位置显示错误
* 当前像素显示与菜单切换不一致 **--->** 初始的菜单判断逻辑存在问题，像素中级位置缺少约束

---

### 2022-03-18

#### 完成工作

* 进行测试，检验`Bug`
* 估计界面显示进行位置调整

#### 开发问题

* 


---

### 2022-03-22

#### 完成工作

* 添加传统模式进行对照
* 记录的实验数据待定，目前以实现文件存储
* 在主界面添加传统模式选择按钮
* 将主菜单的响应与传统绘制界面链接
* 设置界面布局
* 感觉返回按钮有点问题，到时候再调整一下
* 传统模式菜单的样式和绘制模块编写完成
* 剩下就是对选择菜单逻辑的编写

#### 开发问题

* 在主菜单点击传统按钮跳转失败 **--->** 只能从当前活动跳向另一个活动

---

### 2022-03-24

#### 完成工作

* 继续设计传统模式的设计，主要是实现菜单的响应和切换
* 返回按钮暂时还没有写
* **二级菜单的响应** 定义为点击菜单位置触发（其实对于式点击触发还是抬起触发都是可以实现的，但是大部分还是抬起触发，所以在这里设置为抬起触发）
* 将菜单区域（包括二级菜单区域）的设置为画笔 **真空区域（画笔不显示）**

#### 开发问题

* 菜单选择逻辑存在问题以及切换后绘制出现失灵的情况

---

### 2022-03-29

#### 完成工作

* 完善菜单选择逻辑
* 解决菜单切换错误和线条绘制的bug
* 线条的绘制感觉有点不太灵敏
* 完成像素切换的功能
* 对返回键进行调整（之前只是从当前页面跳转到主页面），现在是实现了返回效果

#### 开发问题

* 切换颜色导致之前绘制的线条全部变色（这个问题之前已经解决过，再次出现估计是存在逻辑问题）且无法再次切换颜色 **--->** 调整产生新对象的位置
* 线条绘制错误 **--->** 新产生的对象添加位置存在错误
* 颜色切换一直是红色 **--->** 判断内的要素过多，导致判断逻辑错误
* 线条绘制失灵问题 **--->** 布尔变量赋值失灵，将对点击位置的判断放在对移动的判断，将布尔变量移除

---

### 2022-04-08

#### 完成工作

* 开始进行文件保存测试，文件保存的数据暂时为：
```cpp

1.测试姓名：
2.实验组数；进行总的几次
3.实验组编号：在进行当前组的第几次测试
4.模式切换技术：
5.目标颜色：
6.目标像素：
7.误触发总数：
8.颜色切换错误数：
9.像素切换错误数：
10.模式切换总错误数：

```
* 先实现 ** 5、6、7、8、9、10 ** 前面的需要在主菜单进行布置，暂时放在后面进行实现

#### 开发问题

* 

---

### 2022-04-10

#### 完成工作

* 继续完成数据记录的工作，现在暂时先在传统模式上进行测试
* 进行主界面的样式再设计，需要在之前的基础上进行`用户名` 和 `实验组数` ，`用户名` 由输入框进行输入， `实验组数` 设计为选择栏，顺便对主界面样式进行一些调整
* 将获取实验组数的方式改为输入，因为`Android`的复选菜单不太适用，这个先暂时这样，待讨论
* 对菜单样式的排版进行重新选择，在布局过程中，现有的样式出现排版不美观的情况

#### 开发问题

* 菜单样式调整达不到预期要求

---

### 2022-04-11

#### 完成工作

* 在传统模式下进行数据记录部署，进行数据存储检测
* 将一次实验的结束条件设置为第三个环绘制完成
* 测试完成后的初始化模块（将界面还原为最初的样子）

#### 开发问题

* 在进行测试的时候，当第三个圆环绘制完成后，直接退出到主界面，而不是按逻辑初始化 **--->** 清空画笔的操作有问题，清空之后进行对象添加
* 完成一次绘制后，无法再次触发环的操作 **--->** 对圆环的判断状态进行更新
* 初始化后，会出现一条直线从菜单处与最后一次绘制的位置相连 **--->** 附加一个反应当前状态的变量
* 没有在绘制完成后立即清除画笔 **--->** 在初始化时进行画布清除
* 更新当前像素失败 **--->** 对菜单状态进行更新

---

### 2022-04-12

#### 完成工作

* 解决界面跟新出现的遗留问题
* 开始进行界面输入的数据记录（之前尝试了一下，但是没有达到预期效果）

#### 开发问题

* 将主界面的值传入到文件中是个问题

---

### 2022-04-14

#### 完成工作

* 继续完成从主界面的数据获取程序

#### 开发问题

* 测试者姓名正常传入，但是在存储过程中却是一个 `空字符` **--->** 用户名的存储位置错误，同时还要注意 `activity` 和 `View`的响应顺序

---

### 2022-04-18

#### 待完成任务

* 系统调整修改

```cpp

1.将组数选中定为选择菜单 ---> 已完成
2.传统模式的一级菜单左右宽度减少 1 / 3 ---> 已完成 由最初的 1 / 3 改为 1 / 2
3.单手主辅模式的菜单字体需要调整
4.单手动态模式的一级菜单也需要减少1/3，与传统模式保持一致

```

* 数据记录新增

```cpp

时间记录：
	* 三个画圈时间：手触摸的第一个圆环到第二个圆环出现的时间
	* 模式切换时间：
		传统模式：手画完圆抬起手到选中切换目标
		单手主辅：从双击开始到选中目标
		单手动态：从双击到选中目标
	* 整个操作时间：整个屏幕上第一次触摸到最后抬手的时间

```

* 待修改 **bug**

```cpp

	在传统模式中一开始选择像素再画就会默认为4px ---> 已完成

```

#### 完成工作

* 对主界面的组数选择进行修改，将最初的输入改为下拉菜单

#### 开发问题

* 

---

### 2022-04-19

#### 完成工作

* 调整传统模式下的菜单宽度，调整为 ** 1 / 2 **，对一级菜单的位置也需要进行响应的调整
* 修改在传统界面出现的 **Bug**
* 对提示的像素进行修改
* 对传统界面的选择逻辑进行修改

#### 开发问题

* 修改后的传统模式在选择完像素后无法继续进行绘制 **--->** boolean 变量没有更新

---

### 2022-04-20

#### 完成工作

* 完成进行完当前组数退回到主界面
* 将数据改为单次记录
* 在传统界面中增加对时间数据的记录

#### 开发问题

* 误触发的记录逻辑存在错误（手指按下时会一直增加次数）**--->**  当手指一直按下时，会对`up`一直检测，所以统一放在对手指`down`的检测中

---

### 2022-04-21

#### 完成工作

* 完成传统模式中相关时间数据的采集部署
* 考虑是不是该将初始化的代码段优化一下
* 新增对时间数据记录的相关控制变量，用于准确记录时间，以及约束记录

#### 开发问题

* 

---

### 2022-04-22

#### 完成工作

* 所有的数据记录在传统模式上部署完成，完成测试
* 优化完成测试后的初始化
* 开始对剩下两个模式进行代码更新

#### 开发问题

* 

---

### 2022-04-23

#### 完成工作


#### 开发问题


---


