package com.example.my_nga_fornums.dongman;

public class DongmanInfoBean {
    private int id;
    private String url;
    private int type;
    private String name;
    private String name_cn;
    private String summary;
    private String air_date;
    private int air_weekday;
    private Rating rating;
    private Images images;
    private Collection collection;

    // 内部类，用于表示评分信息
    public static class Rating {
        private int total;
        private double score;
        private Count count;

        // 内部类，用于表示不同评分的数量
        public static class Count {
            private int one;
            private int two;
            private int three;
            private int four;
            private int five;
            private int six;
            private int seven;
            private int eight;
            private int nine;
            private int ten;

            // 省略getter和setter方法

            public int getOne() {
                return one;
            }

            public void setOne(int one) {
                this.one = one;
            }

            public int getTwo() {
                return two;
            }

            public void setTwo(int two) {
                this.two = two;
            }

            public int getThree() {
                return three;
            }

            public void setThree(int three) {
                this.three = three;
            }

            public int getFour() {
                return four;
            }

            public void setFour(int four) {
                this.four = four;
            }

            public int getFive() {
                return five;
            }

            public void setFive(int five) {
                this.five = five;
            }

            public int getSix() {
                return six;
            }

            public void setSix(int six) {
                this.six = six;
            }

            public int getSeven() {
                return seven;
            }

            public void setSeven(int seven) {
                this.seven = seven;
            }

            public int getEight() {
                return eight;
            }

            public void setEight(int eight) {
                this.eight = eight;
            }

            public int getNine() {
                return nine;
            }

            public void setNine(int nine) {
                this.nine = nine;
            }

            public int getTen() {
                return ten;
            }

            public void setTen(int ten) {
                this.ten = ten;
            }
        }

        // 省略getter和setter方法

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public Count getCount() {
            return count;
        }

        public void setCount(Count count) {
            this.count = count;
        }
    }

    // 内部类，用于表示图片信息
    public static class Images {
        private String large;
        private String common;
        private String medium;
        private String small;
        private String grid;

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getCommon() {
            return common;
        }

        public void setCommon(String common) {
            this.common = common;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getGrid() {
            return grid;
        }

        public void setGrid(String grid) {
            this.grid = grid;
        }
// 省略getter和setter方法
    }

    // 内部类，用于表示收藏信息
    public static class Collection {
        private int collect;

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }
// 省略getter和setter方法
    }

    // 省略getter和setter方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getAir_weekday() {
        return air_weekday;
    }

    public void setAir_weekday(int air_weekday) {
        this.air_weekday = air_weekday;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}