create table job
(
  record_id         bigint      auto_increment      not null,
  job_name         varchar(50)                     not null,    --  职务名称
  job_level        tinyint                         not null,    --  职位等级

  primary key (record_id),
  index idx_job_01(job_name)
)comment '职务';

create  table city
(
  record_id         bigint    auto_increment     not null,
  city_code         varchar(12)                  not null,     -- 编码 (省/市)
  city_name         varchar(50)                  not null,     -- 名称
  parent_id         bigint                       not null,     -- 所属上级(上级省份或者上级城市)

  primary key (record_id),
  index idx_city_01(city_code),
  index idx_city_02(city_name),
  index idx_city_03(parent_id)
)comment '市';


create table salon(
  record_id          bigint     auto_increment     not null,
  salon_name         varchar(120)                  not null,               -- 美容院/门店名称
  parent_id          bigint                        not null,               -- 所属美容院:如果是美容院，则 parent_id = 0
  tel                varchar(50)                   not null,               -- 联系电话
  city_id            bigint                        not null,               -- 所属城市（省市区）
  address            varchar(255)                  not null,               -- 地址
  door_2_door        bit                           not null default 0,     -- 是否上门服务:美容院专用
  bed_num            int                           not null default 0,     -- 床位数：门店专用
  area               double(8,2)                    not null default 0.00,  -- 面积：门店专用
  time_open          time                          null,                   -- 营业开始时间：门店专用
  time_close         time                          null,                   -- 营业结束时间：门店专用
  description        varchar(500)                  null,                   -- 简介

  start_score        tinyint                       not null,               -- 启用积分:0 启用   1.停用
  start_handwork     tinyint                       not null,               -- 启用手工费

  primary key (record_id),
  index idx_beauty_salon_01(salon_name),
  index idx_beauty_salon_02(tel),
  index idx_beauty_salon_03(city_id)
)comment '美容院/门店';


create table stuff
(
  record_id          bigint      auto_increment      not null,
  store_id           bigint                          not null,  -- 所属门店
  stuff_name         varchar(50)                     not null,  -- 姓名
  tel                varchar(50)                     not null,  -- 电话
  gender             tinyint                         not null,  -- 性别
  entry_time         datetime                        not null,  -- 入职时间
  work_age           double(3,1)                     not null,  -- 工作年限
  special            varchar(120)                    null,      -- 特长
  dream              varchar(120)                    null,      -- 梦想
  weixin             varchar(50)                     null,      -- 微信
  qq                 varchar(50)                     null,      -- QQ
  address            varchar(120)                    null,      -- 联系地址

  primary key (record_id),
  index idx_stuff_01(store_id),
  index idx_stuff_02(stuff_name),
  index idx_stuff_03(tel),
  index idx_stuff_04(gender)
) comment '员工';


create table stuff_job
(
  record_id         bigint      auto_increment      not null,
  stuff_id          bigint                          not null,   -- 员工编号
  job_id           bigint                          not null,    -- 职位编号

  primary key (record_id),
  index idx_stuff_job_01(stuff_id),
  index idx_stuff_job_02(job_id)
)comment '员工职务';


create table store_room
(
  record_id        bigint        auto_increment    not null,
  store_id         bigint                          not null,    -- 所属门店
  room_name        varchar(50)                     not null,    -- 房间名门
  room_status      tinyint                         not null,    -- 房间状态：0. 启用    1.停用

  primary key (record_id),
  index idx_store_room_01(store_id),
  index idx_store_room_02(room_name),
  index idx_store_room_03(room_status)
)comment '门店房间';


create table service_series
(
  record_id              bigint          auto_increment    not null,
  series_name            varchar(50)                       not null,   -- 系列/类别名称
  parent_id              bigint                            not null,   -- 项目类别：如果本身是一级类别，则parent_id=0
  service_type_status    tinyint                           not null,   -- 状态： 0. 启用   1. 停用

  primary key (record_id),
  index idx_service_class_01(series_name),
  index idx_service_class_02(parent_id)
)comment '项目类别/系列';

create table service
(
  record_id             bigint      auto_increment      not null,
  service_name          varchar(50)                     not null,    -- 项目名称
  service_series_id     bigint                          not null,    -- 项目类别/系列
  card_type             tinyint                         not null,    -- 卡类别: 0 次卡   时效卡：1.月卡   2.季卡  3.半年卡   4.年卡
  service_status        tinyint                         not null,    -- 项目状态：0 启用   1 停用
  expired_time          double(3,1)                     null,        -- 有效期：时效卡专用。自购买之日起，N时间内有效。

  price_market          double(8,2)                     not null,    -- 市场价格：时效卡专用
  price                 double(8,2)                     not null,    -- 办卡价格/优惠价格
  time_total            int                             not null,    -- 包含次数/最多包含次数：-1表示无限次数
  price_single          double(8,2)                     not null,    -- 单次价格/单次耗卡金额
  time_single           int                             not null,    -- 单次时长(分钟):-1表示无限时长
  return_visit          int                             null,        -- 回访天数

  price_handwork        double(8,2)                     not null,    -- 手工费
  price_handwork_bonus  double(8,2)                     not null,    -- 赠送手工费
  score                 int                             not null,    -- 实际积分
  score_bonus           int                             not null,    -- 赠送积分

  description           varchar(500)                    null,        -- 简介

  primary key (record_id),
  index idx_service_card_01(service_series_id),
  index idx_service_card_02(card_type),
  index idx_service_card_03(service_name),
  index idx_service_card_04(service_status)
)comment '次卡/服务项目';

create table service_suite
(
  record_id        bigint     auto_increment         not null,
  suite_name       varchar(50)                       not null,     -- 套卡名称
  price_market     double(8,2)                       not null,     -- 市场价格:汇总service_suite_item里面各项的单价
  price            double(8,2)                       not null,     -- 优惠价格/开卡价格

  time_create      datetime                          not null,     -- 建档时间
  time_expired     datetime                          null,         -- 失效日期：如果为null，则表示无期限
  suite_status     tinyint                           not null,     -- 套卡状态：0 正常   1.停用     2.失效（即已过了有效期）

  description      varchar(500)                      null,

  primary key (record_id),
  index idx_service_suite_01(suite_name),
  index idx_service_suite_02(suite_status)
)comment '套卡/服务套餐';


create table service_suite_item
(
  record_id        bigint    auto_increment       not null,
  service_id       bigint                         not null,   -- 服务项目编号
  times            int                            not null,   -- 次数

  -- price_single  double(8,2)                    not null,   -- 单次价格：直接引用service里面的价格

  primary key (record_id),
  index idx_service_card_suite_item_01(service_id)
) comment '套卡明细';


create table vip_suite
(
  record_id                   bigint     auto_increment        not null,
  suite_name                  varchar(50)                      not null,    -- 充值卡名称
  price                       double(8,2)                      not null,    -- 充值面额
  vip_suite_status            tinyint                          not null,    -- 记录状态：0.启用   1.停用
  discount_time                tinyint                         not null,    -- 单次折扣
  discount_period              tinyint                         not null,    -- 疗程折扣
  discount_production          tinyint                         not null,    -- 产品折扣
  description                 varchar(500)                     not null,    -- 介绍

  primary key (record_id),
  index idx_vip_suite_01(suite_name)
)comment '充值卡';

create table vip_suite_item
(
  record_id                 bigint     auto_increment         not null,
  record_type               tinyint                           not null,  -- 记录类型:0.单次折扣  1.疗程折扣  2.产品折扣
  item_id                   bigint                            not null,  -- 折扣项目：服务/产品

  primary key (record_id),
  index idx_vip_suite_item_01(record_type),
  index idx_vip_suite_item_02(item_id)
)comment '充值卡折扣项目';

create table product_series
(
  record_id              bigint      auto_increment           not null,
  series_name            varchar(50)                          not null,   -- 品牌/系列名称
  parent_id              bigint                               not null,   -- 所属品牌:如果本身为品牌，则parent_id=0
  record_status          tinyint                              not null,   -- 记录状态:0.启用  1.停用

  primary key (record_id),
  index idx_product_series_01(series_name),
  index idx_product_series_02(parent_id)
)comment '产品品牌/系列';

create table product
(
  record_id               bigint       auto_increment           not null,
  product_name            varchar(50)                           not null,   -- 产品名称
  product_class           tinyint                               not null,   -- 产品类型: 0.客装   1.院装   2.易耗品
  product_series_id       bigint                                not null,   -- 产品品牌/系列
  price_market            double(8,2)                           not null,   -- 市场价
  price                   double(8,2)                           not null,   -- 优惠价

  product_code            varchar(20)                           null,       -- 产品编号
  specification           int                                   null,       -- 规格:就是数量，比如3kg/瓶
  specification_unit      tinyint                               null,       -- 规格单位：0. g(克)   1.Kg(千克)  2.ml(毫升)  3.L(升)
  product_unit_id         bigint                                not null,   -- 单位：瓶/袋/包等
  part_of_applicable_id   bigint                                not null,   -- 适用部位
  efficiency_tag_id       bigint                                not null,   -- 功效

  bar_code                varchar(2048)                         null,       -- 二维码/条形码
  shelf_life              tinyint                               not null,   -- 保质期(月)
  day_of_pre_warning      int                                   not null,   -- 产品有效期预警（天）
  stock_of_pre_warning    int                                   not null,   -- 库存预警数量

  record_status           tinyint                               not null,   -- 记录状态：0.启用   1. 停用
  description             varchar(500)                          null,

  primary key (record_id),
  index idx_product_01(product_name),
  index idx_product_02(product_class),
  index idx_product_03(product_series_id),
  index idx_product_04(bar_code),
  index idx_product_05(shelf_life),
  index idx_product_06(day_of_pre_warning),
  index idx_product_07(stock_of_pre_warning)
) comment '产品';


create table product_unit
(
  record_id              bigint        auto_increment          not null,
  unit_name              varchar(50)                           not null,

  primary key (record_id),
  index idx_product_unit_01(unit_name)
)comment '产品单位';

create table body_part
(
  record_id             bigint         auto_increment          not null ,
  part_name             varchar(50)                            not null,  -- 部位名称

  primary key (record_id),
  index idx_body_part_01(part_name)
)comment '部位';


create table tag
(
  record_id             bigint        auto_increment          not null,
  record_type           tinyint                               not null, -- 0.功效标签   1.客户标签
  tag_name              varchar(50)                           not null, -- 标签

  primary key (record_id),
  index idx_tag_01(record_type),
  index idx_tag_02(tag_name)
)comment '标签';


create table pictures
(
  record_id        bigint    auto_increment       not null ,
  master_data_id   bigint                         not null,     -- 主记录编号
  record_type      tinyint                        not null,     -- 记录类型: 0.美容院（门店）  1. 员工    2. 项目/卡   3.套卡   4.充值卡  5.产品    6.会员/顾客
  pic_type         tinyint                        not null,     -- 照片类型: 0.普通照片   1.营业执照（1张)    2.身份证（要有2张，正反面各1张）  3.开户许可(1张)
  pic_url          varchar(255)                   not null,     -- 照片: 如果以http|https开头，则是外部绝对地址，否则为内部相对地址。

  primary key (record_id),
  index idx_pictures_01(master_data_id),
  index idx_pictures_02(record_type),
  index idx_pictures_02(pic_type)
) comment '系统照片';


create table member
(
  record_id           bigint      auto_increment   not null ,
  store_id            bigint                       not null,   -- 所属美容院/门店(档案来源)
  member_name         varchar(50)                  not null,   -- 姓名
  tel                 varchar(50)                  not null,   -- 电话
  gender              tinyint                      not null,   -- 性别
  weixin              varchar(50)                  not null,   -- 微信
  birthday            datetime                     null,       -- 生日
  zodiac              tinyint                      null,       -- 星座
  member_grade_id     bigint                       not null,   -- 分类
  -- member_tag                                                -- 会员标签

  blood_type          tinyint                      null,       -- 血型
  height              int                          null,       -- 身高
  weight              int                          null,       -- 体重

  last_day_of_menses  datetime                     null,       -- 上次月经时间
  cycle_of_menses     tinyint                      null,       -- 月经周期
  period_of_menses    tinyint                      null,       -- 经期长度
  remark_of_menses    varchar(500)                 null,       -- 月经备注

  profession          varchar(50)                  null,       -- 职业
  city_id             bigint                       null,       -- 城市（省市区）
  address             varchar(255)                 null,       -- 地址
  email               varchar(255)                 null,       -- 邮箱

  member_code         char(10)                     null,       -- 会员编码
  introducer          varchar(50)                  null,       -- 介绍人
  primary_beautician  bigint                       null,       -- 负责美容师
  entry_time          datetime                     null,       -- 入店时间
  customer_pipe_id    bigint                       null,       -- 来源渠道

  balance                double(10,2)               not null,   -- 账户总余额
  amount_consumer        double(10,2)               not null,   -- 总消费
  amount_charge          double(10,2)               not null,   -- 总充值
  count_discount_ticket  tinyint                    not null,   -- 优惠券张数

  primary key (record_id),
  index idx_member_01(store_id),
  index idx_member_02(member_name),
  index idx_member_03(birthday),
  index idx_member_04(tel),
  index idx_member_05(primary_beautician),
  index idx_member_06(introducer)
) comment '会员';

create  table member_grade
(
  record_id           bigint      auto_increment   not null,
  grade_name          varchar(10)                  not null,
  grade_level         tinyint                      not null,

  primary key (record_id),
  index idx_member_grade_01(grade_name)
)comment '会员级别';

create table member_tag
(
  record_id          bigint     auto_increment  not null,
  member_id          bigint                     not null,
  tag_id             bigint                     not null,

  primary key (record_id),
  index idx_member_tag_01(member_id),
  index idx_member_tag_02(tag_id)
)comment '会员标签';




