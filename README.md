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

* 完成Android的基础理论
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

