CREATE DATABASE `erciyuan` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */


create table articles
(
    ArticleID     int auto_increment
        primary key,
    UniqueKey     varchar(255) not null,
    Title         varchar(255) not null,
    Date          datetime     not null,
    Category      varchar(100) null,
    AuthorName    varchar(100) null,
    URL           varchar(255) null,
    ThumbnailPicS varchar(255) null,
    IsContent     tinyint(1)   null,
    constraint UniqueKey
        unique (UniqueKey)
);

create table articles
(
    ArticleID     int auto_increment
        primary key,
    UniqueKey     varchar(255) not null,
    Title         varchar(255) not null,
    Date          datetime     not null,
    Category      varchar(100) null,
    AuthorName    varchar(100) null,
    URL           varchar(255) null,
    ThumbnailPicS varchar(255) null,
    IsContent     tinyint(1)   null,
    constraint UniqueKey
        unique (UniqueKey)
);

INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (1, '605ad288c08b0c49ebef2dde1e75c9ef', '[新闻] 动工50周年纪念作「夜晚的水母不会游泳」原创TV动画 24年4月', '2024-06-26 17:29:00', 'ACG新闻', '游民星空', 'https://bbs.saraba1st.com/2b/thread-2125702-1-1.html', 'https://p.sda1.dev/16/94249546a480fd8cb2eb95e032513c03/mainimg5.jpg', 1);
INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (2, 'e2e861d65154b9e0c321d296ab38fa6a', '轻小说作家的日常写作状态:刷推特才是最重要的!', '2024-06-26 17:29:00', 'ACG新闻', '卡卡洛普', 'https://acg.gamersky.com/news/202303/1579070.shtml', 'https://ts1.cn.mm.bing.net/th/id/R-C.661f851dea7c73141c461ea6bf2a60de?rik=zksgkhQdqycfXQ&riu=http%3a%2f%2fi3.img.969g.com%2fdown%2fimgx2013%2f10%2f25%2f255_111351_0ee79.jpg&ehk=JrLoSRCYwwtlAkdcR%2bUOF7Wk1V1i5SLhM4Frsa0kjho%3d&risl=&pid=ImgRaw&r=0', 1);
INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (3, '87f7f428add078d4c6bdd7ab70ba63c5', '<名侦探柯南:黑铁的鱼影>中国台湾7月6日上映 中文预告公开', '2024-06-26 17:22:00', 'ACG新闻', '动漫星空', 'https://acg.gamersky.com/news/202303/1575839.shtml', 'https://ts1.cn.mm.bing.net/th/id/R-C.661f851dea7c73141c461ea6bf2a60de?rik=zksgkhQdqycfXQ&riu=http%3a%2f%2fi3.img.969g.com%2fdown%2fimgx2013%2f10%2f25%2f255_111351_0ee79.jpg&ehk=JrLoSRCYwwtlAkdcR%2bUOF7Wk1V1i5SLhM4Frsa0kjho%3d&risl=&pid=ImgRaw&r=0', 1);
INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (4, 'b8e1c96764c883de6bb21c3805f9c09d', '商城县公安局依法查处一批网络谣言案件', '2024-06-26 16:26:00', 'ACG新闻', '卡卡洛普', 'https://mini.eastday.com/mobile/240626162618368644456.html', 'https://ts1.cn.mm.bing.net/th/id/R-C.661f851dea7c73141c461ea6bf2a60de?rik=zksgkhQdqycfXQ&riu=http%3a%2f%2fi3.img.969g.com%2fdown%2fimgx2013%2f10%2f25%2f255_111351_0ee79.jpg&ehk=JrLoSRCYwwtlAkdcR%2bUOF7Wk1V1i5SLhM4Frsa0kjho%3d&risl=&pid=ImgRaw&r=0', 1);
INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (6, '605ad288c080c49ebef2dde1e75c9ef', '<罗小黑>角色原型被弃养 网友怒骂:吃猫血馒头', '2024-06-26 17:29:00', 'ACG新闻', '游民星空', 'https://acg.gamersky.com/news/202406/1779372.shtml', 'https://ts1.cn.mm.bing.net/th/id/R-C.661f851dea7c73141c461ea6bf2a60de?rik=zksgkhQdqycfXQ&riu=http%3a%2f%2fi3.img.969g.com%2fdown%2fimgx2013%2f10%2f25%2f255_111351_0ee79.jpg&ehk=JrLoSRCYwwtlAkdcR%2bUOF7Wk1V1i5SLhM4Frsa0kjho%3d&risl=&pid=ImgRaw&r=0', 1);
INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (7, '605ad288c080c349ebef2dde1e75c9ef', '<银河铁道999>作者松本零士因病去世 享年85岁', '2024-06-26 17:29:00', 'ACG新闻', '游民星空', 'https://acg.gamersky.com/news/202302/1568674.shtml', 'https://www.gamersky.com/showimage/id_gamersky.shtml?https://img1.gamersky.com/upimg/pic/2023/02/20/origin_202302201032414125.jpg', 1);
INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (8, '605ad288c1080c49ebef2dde21e75c9ef', '<罗小黑>角色原型被弃养 网友怒骂:吃猫血馒头', '2024-06-26 17:29:00', 'ACG新闻', '游民星空', 'https://acg.gamersky.com/news/202406/1779372.shtml', 'https://ts1.cn.mm.bing.net/th/id/R-C.661f851dea7c73141c461ea6bf2a60de?rik=zksgkhQdqycfXQ&riu=http%3a%2f%2fi3.img.969g.com%2fdown%2fimgx2013%2f10%2f25%2f255_111351_0ee79.jpg&ehk=JrLoSRCYwwtlAkdcR%2bUOF7Wk1V1i5SLhM4Frsa0kjho%3d&risl=&pid=ImgRaw&r=0', 1);
INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (9, '605ad288c080c49ebef412dde1e75c9ef', '<罗小黑>角色原型被弃养 网友怒骂:吃猫血馒头', '2024-06-26 17:29:00', 'ACG新闻', '游民星空', 'https://acg.gamersky.com/news/202406/1779372.shtml', 'https://ts1.cn.mm.bing.net/th/id/R-C.661f851dea7c73141c461ea6bf2a60de?rik=zksgkhQdqycfXQ&riu=http%3a%2f%2fi3.img.969g.com%2fdown%2fimgx2013%2f10%2f25%2f255_111351_0ee79.jpg&ehk=JrLoSRCYwwtlAkdcR%2bUOF7Wk1V1i5SLhM4Frsa0kjho%3d&risl=&pid=ImgRaw&r=0', 1);
INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (10, '605ad288c080c49ebef2dde1e75c41129ef', '<罗小黑>角色原型被弃养 网友怒骂:吃猫血馒头', '2024-06-26 17:29:00', 'ACG新闻', '游民星空', 'https://acg.gamersky.com/news/202406/1779372.shtml', 'https://ts1.cn.mm.bing.net/th/id/R-C.661f851dea7c73141c461ea6bf2a60de?rik=zksgkhQdqycfXQ&riu=http%3a%2f%2fi3.img.969g.com%2fdown%2fimgx2013%2f10%2f25%2f255_111351_0ee79.jpg&ehk=JrLoSRCYwwtlAkdcR%2bUOF7Wk1V1i5SLhM4Frsa0kjho%3d&risl=&pid=ImgRaw&r=0', 1);
INSERT INTO erciyuan.articles (ArticleID, UniqueKey, Title, Date, Category, AuthorName, URL, ThumbnailPicS, IsContent) VALUES (11, '65ad288c080c49ebef2d5de1e75c41129ef', '不止官方停更 连盗版网站都不更新<三体>动画了', '2024-06-26 17:29:00', 'ACG新闻', '游民星空', 'https://acg.gamersky.com/news/202302/1567610.shtml', 'https://www.gamersky.com/showimage/id_gamersky.shtml?https://img1.gamersky.com/upimg/pic/2023/02/16/origin_202302161710329974.jpg', 1);
