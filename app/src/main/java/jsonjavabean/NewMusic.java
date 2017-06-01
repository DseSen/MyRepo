package jsonjavabean;

import java.util.List;

/**
 * Created by 啊丁 on 2017/4/1.
 */

public class NewMusic {

    private List<SongListBean> song_list;

    public List<SongListBean> getSong_list() {
        return song_list;
    }

    public void setSong_list(List<SongListBean> song_list) {
        this.song_list = song_list;
    }

    public static class SongListBean {
        /**
         * song_id : 102723216
         * title : Lights Up
         * ting_uid : 8553
         * artist_id : 892
         * author : 方大同
         * album_id : 114904793
         * album_title : 危险世界
         * language : 英语
         * pic_big : http://musicdata.baidu.com/data2/pic/117108823/117108823.jpg@s_0,w_150
         * pic_small : http://musicdata.baidu.com/data2/pic/117108823/117108823.jpg@s_0,w_90
         * country : 港台
         * area : 1
         * publishtime : 2014-04-11
         * album_no : 16
         * lrclink : /data2/lrc/ae46ce5987d2b96032345936765114c7/294533963/294533963.lrc
         * versions :
         * copy_type : 0
         * file_duration : 295
         * hot :
         * charge : 0
         * havehigh : 2
         * all_artist_ting_uid : 8553
         * is_first_publish : 0
         * pic_premium :
         * pic_huge :
         * pic_singer :
         * has_mv : 0
         * learn : 0
         * song_source :
         * all_rate : 24,64,128,192,256,320,flac
         * resource_type : 0
         * piao_id : 0
         * korean_bb_song : 0
         * resource_type_ext : 0
         * mv_provider : 0000000000
         * main_song_id : 102723216
         * si_presale_flag : 0
         * del_status : 0
         * avatar_s500 : http://qukufile2.qianqian.com/data2/pic/246679913/246679913.jpg@s_0,w_500
         * avatar_s60 : http://qukufile2.qianqian.com/data2/pic/246679913/246679913.jpg@s_0,w_60
         * translatename :
         * pic_s130 : http://qukufile2.qianqian.com/data2/pic/117108823/117108823.jpg@s_0,w_130
         * license_number :
         * group_id : 106495355
         * encrypted_songid :
         * avatar_s180 : http://qukufile2.qianqian.com/data2/pic/246679913/246679913.jpg@s_0,w_180
         * synonym :
         * korean_bb_offtime : 0000-00-00
         * song_relate_ids :
         * avatar_s130 : http://qukufile2.qianqian.com/data2/pic/246679913/246679913.jpg@s_0,w_130
         * yyr_song_id : 0
         * bitrate_fee : {"0":"0|0","1":"0|0"}
         * pic_s180 : http://qukufile2.qianqian.com/data2/pic/117108823/117108823.jpg@s_0,w_180
         * si_market_price : 0.00
         * pic_s1000 : http://qukufile2.qianqian.com/data2/pic/117108823/117108823.jpg@s_0,w_1000
         * is_new : 0
         * pic_radio : http://musicdata.baidu.com/data2/pic/117108823/117108823.jpg@s_0,w_300
         * has_filmtv : 0
         * si_proxycompany : 太合麦田（天津）音乐有限公司
         * is_hot : 0
         * info : “Soulboy Lights Up方大同世界巡回演唱会”主题歌
         * total_listen_nums : 1826577
         * high_rate : 320
         * avatar_small : http://qukufile2.qianqian.com/data2/pic/246679913/246679913.jpg@s_0,w_48
         * source : web
         * distribution : 0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000
         * relate_status : 0
         * avatar_big : http://qukufile2.qianqian.com/data2/pic/246679913/246679913.jpg@s_0,w_240
         * avatar_middle : http://qukufile2.qianqian.com/data2/pic/246679913/246679913.jpg@s_0,w_120
         * avatar_mini : http://qukufile2.qianqian.com/data2/pic/246679913/246679913.jpg@s_0,w_20
         * aliasname :
         * link :
         * style : 流行
         * si_price : 0.00
         * toneid : 600907000002300225
         * compose : 方大同,Fergus Chow
         * all_artist_id : 892
         * updatetime : 2016-12-21 14:39:01
         * avatar_s300 : http://qukufile2.qianqian.com/data2/pic/246679913/246679913.jpg@s_0,w_300
         * is_ksong : 0
         * lossless_audio : 1
         * songwriting : 方大同,Fergus Chow
         * lrclink_k :
         * listen_nums : 28
         * pic_s500 : http://qukufile2.qianqian.com/data2/pic/117108823/117108823.jpg@s_0,w_500
         * resource_provider : 1
         * complaint_times : 0
         * mis_create_time : 2013-11-20 12:03:33
         * has_mv_mobile : 0
         */

        private String song_id;
        private String title;
        private String ting_uid;
        private String artist_id;
        private String author;
        private String album_id;
        private String album_title;
        private String language;
        private String pic_big;
        private String pic_small;
        private String country;
        private String area;
        private String publishtime;
        private String album_no;
        private String lrclink;
        private String versions;
        private String copy_type;
        private String file_duration;
        private String hot;
        private int charge;
        private int havehigh;
        private String all_artist_ting_uid;
        private String is_first_publish;
        private String pic_premium;
        private String pic_huge;
        private String pic_singer;
        private int has_mv;
        private int learn;
        private String song_source;
        private String all_rate;
        private String resource_type;
        private String piao_id;
        private String korean_bb_song;
        private String resource_type_ext;
        private String mv_provider;
        private String main_song_id;
        private String si_presale_flag;
        private String del_status;
        private String avatar_s500;
        private String avatar_s60;
        private String translatename;
        private String pic_s130;
        private String license_number;
        private String group_id;
        private String encrypted_songid;
        private String avatar_s180;
        private String synonym;
        private String korean_bb_offtime;
        private String song_relate_ids;
        private String avatar_s130;
        private String yyr_song_id;
        private String bitrate_fee;
        private String pic_s180;
        private String si_market_price;
        private String pic_s1000;
        private String is_new;
        private String pic_radio;
        private String has_filmtv;
        private String si_proxycompany;
        private String is_hot;
        private String info;
        private String total_listen_nums;
        private String high_rate;
        private String avatar_small;
        private String source;
        private String distribution;
        private String relate_status;
        private String avatar_big;
        private String avatar_middle;
        private String avatar_mini;
        private String aliasname;
        private String link;
        private String style;
        private String si_price;
        private String toneid;
        private String compose;
        private String all_artist_id;
        private String updatetime;
        private String avatar_s300;
        private String is_ksong;
        private String lossless_audio;
        private String songwriting;
        private String lrclink_k;
        private String listen_nums;
        private String pic_s500;
        private String resource_provider;
        private String complaint_times;
        private String mis_create_time;
        private int has_mv_mobile;

        public String getSong_id() {
            return song_id;
        }

        public void setSong_id(String song_id) {
            this.song_id = song_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTing_uid() {
            return ting_uid;
        }

        public void setTing_uid(String ting_uid) {
            this.ting_uid = ting_uid;
        }

        public String getArtist_id() {
            return artist_id;
        }

        public void setArtist_id(String artist_id) {
            this.artist_id = artist_id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(String album_id) {
            this.album_id = album_id;
        }

        public String getAlbum_title() {
            return album_title;
        }

        public void setAlbum_title(String album_title) {
            this.album_title = album_title;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getPic_big() {
            return pic_big;
        }

        public void setPic_big(String pic_big) {
            this.pic_big = pic_big;
        }

        public String getPic_small() {
            return pic_small;
        }

        public void setPic_small(String pic_small) {
            this.pic_small = pic_small;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getPublishtime() {
            return publishtime;
        }

        public void setPublishtime(String publishtime) {
            this.publishtime = publishtime;
        }

        public String getAlbum_no() {
            return album_no;
        }

        public void setAlbum_no(String album_no) {
            this.album_no = album_no;
        }

        public String getLrclink() {
            return lrclink;
        }

        public void setLrclink(String lrclink) {
            this.lrclink = lrclink;
        }

        public String getVersions() {
            return versions;
        }

        public void setVersions(String versions) {
            this.versions = versions;
        }

        public String getCopy_type() {
            return copy_type;
        }

        public void setCopy_type(String copy_type) {
            this.copy_type = copy_type;
        }

        public String getFile_duration() {
            return file_duration;
        }

        public void setFile_duration(String file_duration) {
            this.file_duration = file_duration;
        }

        public String getHot() {
            return hot;
        }

        public void setHot(String hot) {
            this.hot = hot;
        }

        public int getCharge() {
            return charge;
        }

        public void setCharge(int charge) {
            this.charge = charge;
        }

        public int getHavehigh() {
            return havehigh;
        }

        public void setHavehigh(int havehigh) {
            this.havehigh = havehigh;
        }

        public String getAll_artist_ting_uid() {
            return all_artist_ting_uid;
        }

        public void setAll_artist_ting_uid(String all_artist_ting_uid) {
            this.all_artist_ting_uid = all_artist_ting_uid;
        }

        public String getIs_first_publish() {
            return is_first_publish;
        }

        public void setIs_first_publish(String is_first_publish) {
            this.is_first_publish = is_first_publish;
        }

        public String getPic_premium() {
            return pic_premium;
        }

        public void setPic_premium(String pic_premium) {
            this.pic_premium = pic_premium;
        }

        public String getPic_huge() {
            return pic_huge;
        }

        public void setPic_huge(String pic_huge) {
            this.pic_huge = pic_huge;
        }

        public String getPic_singer() {
            return pic_singer;
        }

        public void setPic_singer(String pic_singer) {
            this.pic_singer = pic_singer;
        }

        public int getHas_mv() {
            return has_mv;
        }

        public void setHas_mv(int has_mv) {
            this.has_mv = has_mv;
        }

        public int getLearn() {
            return learn;
        }

        public void setLearn(int learn) {
            this.learn = learn;
        }

        public String getSong_source() {
            return song_source;
        }

        public void setSong_source(String song_source) {
            this.song_source = song_source;
        }

        public String getAll_rate() {
            return all_rate;
        }

        public void setAll_rate(String all_rate) {
            this.all_rate = all_rate;
        }

        public String getResource_type() {
            return resource_type;
        }

        public void setResource_type(String resource_type) {
            this.resource_type = resource_type;
        }

        public String getPiao_id() {
            return piao_id;
        }

        public void setPiao_id(String piao_id) {
            this.piao_id = piao_id;
        }

        public String getKorean_bb_song() {
            return korean_bb_song;
        }

        public void setKorean_bb_song(String korean_bb_song) {
            this.korean_bb_song = korean_bb_song;
        }

        public String getResource_type_ext() {
            return resource_type_ext;
        }

        public void setResource_type_ext(String resource_type_ext) {
            this.resource_type_ext = resource_type_ext;
        }

        public String getMv_provider() {
            return mv_provider;
        }

        public void setMv_provider(String mv_provider) {
            this.mv_provider = mv_provider;
        }

        public String getMain_song_id() {
            return main_song_id;
        }

        public void setMain_song_id(String main_song_id) {
            this.main_song_id = main_song_id;
        }

        public String getSi_presale_flag() {
            return si_presale_flag;
        }

        public void setSi_presale_flag(String si_presale_flag) {
            this.si_presale_flag = si_presale_flag;
        }

        public String getDel_status() {
            return del_status;
        }

        public void setDel_status(String del_status) {
            this.del_status = del_status;
        }

        public String getAvatar_s500() {
            return avatar_s500;
        }

        public void setAvatar_s500(String avatar_s500) {
            this.avatar_s500 = avatar_s500;
        }

        public String getAvatar_s60() {
            return avatar_s60;
        }

        public void setAvatar_s60(String avatar_s60) {
            this.avatar_s60 = avatar_s60;
        }

        public String getTranslatename() {
            return translatename;
        }

        public void setTranslatename(String translatename) {
            this.translatename = translatename;
        }

        public String getPic_s130() {
            return pic_s130;
        }

        public void setPic_s130(String pic_s130) {
            this.pic_s130 = pic_s130;
        }

        public String getLicense_number() {
            return license_number;
        }

        public void setLicense_number(String license_number) {
            this.license_number = license_number;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getEncrypted_songid() {
            return encrypted_songid;
        }

        public void setEncrypted_songid(String encrypted_songid) {
            this.encrypted_songid = encrypted_songid;
        }

        public String getAvatar_s180() {
            return avatar_s180;
        }

        public void setAvatar_s180(String avatar_s180) {
            this.avatar_s180 = avatar_s180;
        }

        public String getSynonym() {
            return synonym;
        }

        public void setSynonym(String synonym) {
            this.synonym = synonym;
        }

        public String getKorean_bb_offtime() {
            return korean_bb_offtime;
        }

        public void setKorean_bb_offtime(String korean_bb_offtime) {
            this.korean_bb_offtime = korean_bb_offtime;
        }

        public String getSong_relate_ids() {
            return song_relate_ids;
        }

        public void setSong_relate_ids(String song_relate_ids) {
            this.song_relate_ids = song_relate_ids;
        }

        public String getAvatar_s130() {
            return avatar_s130;
        }

        public void setAvatar_s130(String avatar_s130) {
            this.avatar_s130 = avatar_s130;
        }

        public String getYyr_song_id() {
            return yyr_song_id;
        }

        public void setYyr_song_id(String yyr_song_id) {
            this.yyr_song_id = yyr_song_id;
        }

        public String getBitrate_fee() {
            return bitrate_fee;
        }

        public void setBitrate_fee(String bitrate_fee) {
            this.bitrate_fee = bitrate_fee;
        }

        public String getPic_s180() {
            return pic_s180;
        }

        public void setPic_s180(String pic_s180) {
            this.pic_s180 = pic_s180;
        }

        public String getSi_market_price() {
            return si_market_price;
        }

        public void setSi_market_price(String si_market_price) {
            this.si_market_price = si_market_price;
        }

        public String getPic_s1000() {
            return pic_s1000;
        }

        public void setPic_s1000(String pic_s1000) {
            this.pic_s1000 = pic_s1000;
        }

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        public String getPic_radio() {
            return pic_radio;
        }

        public void setPic_radio(String pic_radio) {
            this.pic_radio = pic_radio;
        }

        public String getHas_filmtv() {
            return has_filmtv;
        }

        public void setHas_filmtv(String has_filmtv) {
            this.has_filmtv = has_filmtv;
        }

        public String getSi_proxycompany() {
            return si_proxycompany;
        }

        public void setSi_proxycompany(String si_proxycompany) {
            this.si_proxycompany = si_proxycompany;
        }

        public String getIs_hot() {
            return is_hot;
        }

        public void setIs_hot(String is_hot) {
            this.is_hot = is_hot;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getTotal_listen_nums() {
            return total_listen_nums;
        }

        public void setTotal_listen_nums(String total_listen_nums) {
            this.total_listen_nums = total_listen_nums;
        }

        public String getHigh_rate() {
            return high_rate;
        }

        public void setHigh_rate(String high_rate) {
            this.high_rate = high_rate;
        }

        public String getAvatar_small() {
            return avatar_small;
        }

        public void setAvatar_small(String avatar_small) {
            this.avatar_small = avatar_small;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getDistribution() {
            return distribution;
        }

        public void setDistribution(String distribution) {
            this.distribution = distribution;
        }

        public String getRelate_status() {
            return relate_status;
        }

        public void setRelate_status(String relate_status) {
            this.relate_status = relate_status;
        }

        public String getAvatar_big() {
            return avatar_big;
        }

        public void setAvatar_big(String avatar_big) {
            this.avatar_big = avatar_big;
        }

        public String getAvatar_middle() {
            return avatar_middle;
        }

        public void setAvatar_middle(String avatar_middle) {
            this.avatar_middle = avatar_middle;
        }

        public String getAvatar_mini() {
            return avatar_mini;
        }

        public void setAvatar_mini(String avatar_mini) {
            this.avatar_mini = avatar_mini;
        }

        public String getAliasname() {
            return aliasname;
        }

        public void setAliasname(String aliasname) {
            this.aliasname = aliasname;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getStyle() {
            return style;
        }

        public void setStyle(String style) {
            this.style = style;
        }

        public String getSi_price() {
            return si_price;
        }

        public void setSi_price(String si_price) {
            this.si_price = si_price;
        }

        public String getToneid() {
            return toneid;
        }

        public void setToneid(String toneid) {
            this.toneid = toneid;
        }

        public String getCompose() {
            return compose;
        }

        public void setCompose(String compose) {
            this.compose = compose;
        }

        public String getAll_artist_id() {
            return all_artist_id;
        }

        public void setAll_artist_id(String all_artist_id) {
            this.all_artist_id = all_artist_id;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getAvatar_s300() {
            return avatar_s300;
        }

        public void setAvatar_s300(String avatar_s300) {
            this.avatar_s300 = avatar_s300;
        }

        public String getIs_ksong() {
            return is_ksong;
        }

        public void setIs_ksong(String is_ksong) {
            this.is_ksong = is_ksong;
        }

        public String getLossless_audio() {
            return lossless_audio;
        }

        public void setLossless_audio(String lossless_audio) {
            this.lossless_audio = lossless_audio;
        }

        public String getSongwriting() {
            return songwriting;
        }

        public void setSongwriting(String songwriting) {
            this.songwriting = songwriting;
        }

        public String getLrclink_k() {
            return lrclink_k;
        }

        public void setLrclink_k(String lrclink_k) {
            this.lrclink_k = lrclink_k;
        }

        public String getListen_nums() {
            return listen_nums;
        }

        public void setListen_nums(String listen_nums) {
            this.listen_nums = listen_nums;
        }

        public String getPic_s500() {
            return pic_s500;
        }

        public void setPic_s500(String pic_s500) {
            this.pic_s500 = pic_s500;
        }

        public String getResource_provider() {
            return resource_provider;
        }

        public void setResource_provider(String resource_provider) {
            this.resource_provider = resource_provider;
        }

        public String getComplaint_times() {
            return complaint_times;
        }

        public void setComplaint_times(String complaint_times) {
            this.complaint_times = complaint_times;
        }

        public String getMis_create_time() {
            return mis_create_time;
        }

        public void setMis_create_time(String mis_create_time) {
            this.mis_create_time = mis_create_time;
        }

        public int getHas_mv_mobile() {
            return has_mv_mobile;
        }

        public void setHas_mv_mobile(int has_mv_mobile) {
            this.has_mv_mobile = has_mv_mobile;
        }
    }
}
