package jsonjavabean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 啊丁 on 2017/5/24.
 */

public class MvBeans {

    /**
     * error_code : 22000
     * result : {"video_info":{"video_id":"334807047","mv_id":"334807046","provider":"11","sourcepath":null,"thumbnail":"http://qukufile2.qianqian.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_160","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_240","del_status":"0","distribution":"0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000"},"files":{"31":{"video_file_id":"334807049","video_id":"334807047","definition":"31","file_link":"http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=8ba0f12fd0b003a59c2a543283853183&tvid=676216800","file_format":"","file_extension":"mp4","file_duration":"232","file_size":"0","source_path":"http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=8ba0f12fd0b003a59c2a543283853183&tvid=676216800"}},"min_definition":"31","max_definition":"31","mv_info":{"mv_id":"334807046","all_artist_id":"48865","title":"Consommez (Tryo détourne Bercy 2009) (Live Video)","aliastitle":"","subtitle":"Tryo - Consommez (Tryo détourne Bercy 2009) (Live Video)","play_nums":"0","publishtime":"1970-01-01","del_status":"0","artist_list":[{"artist_id":"48865","ting_uid":"88009156","artist_name":"Tryo","artist_480_800":"","artist_640_1136":"","avatar_small":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_48","avatar_mini":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_20","avatar_s180":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_180","avatar_s300":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_300","avatar_s500":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_500","del_status":"0"}],"artist_id":"48865","thumbnail":"http://musicdata.baidu.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_160","thumbnail3":"http://business.cdn.qianqian.com/baidumisic","thumbnail2":"http://musicdata.baidu.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_240","artist":"Tryo","provider":"11"},"share_url":"http://music.baidu.com/cms/webview/sharevideo?video_id=334807046"}
     */

    private int error_code;
    private ResultBean result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * video_info : {"video_id":"334807047","mv_id":"334807046","provider":"11","sourcepath":null,"thumbnail":"http://qukufile2.qianqian.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_160","thumbnail2":"http://qukufile2.qianqian.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_240","del_status":"0","distribution":"0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000"}
         * files : {"31":{"video_file_id":"334807049","video_id":"334807047","definition":"31","file_link":"http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=8ba0f12fd0b003a59c2a543283853183&tvid=676216800","file_format":"","file_extension":"mp4","file_duration":"232","file_size":"0","source_path":"http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=8ba0f12fd0b003a59c2a543283853183&tvid=676216800"}}
         * min_definition : 31
         * max_definition : 31
         * mv_info : {"mv_id":"334807046","all_artist_id":"48865","title":"Consommez (Tryo détourne Bercy 2009) (Live Video)","aliastitle":"","subtitle":"Tryo - Consommez (Tryo détourne Bercy 2009) (Live Video)","play_nums":"0","publishtime":"1970-01-01","del_status":"0","artist_list":[{"artist_id":"48865","ting_uid":"88009156","artist_name":"Tryo","artist_480_800":"","artist_640_1136":"","avatar_small":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_48","avatar_mini":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_20","avatar_s180":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_180","avatar_s300":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_300","avatar_s500":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_500","del_status":"0"}],"artist_id":"48865","thumbnail":"http://musicdata.baidu.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_160","thumbnail3":"http://business.cdn.qianqian.com/baidumisic","thumbnail2":"http://musicdata.baidu.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_240","artist":"Tryo","provider":"11"}
         * share_url : http://music.baidu.com/cms/webview/sharevideo?video_id=334807046
         */

        private VideoInfoBean video_info;
        private FilesBean files;
        private String min_definition;
        private String max_definition;
        private MvInfoBean mv_info;
        private String share_url;

        public VideoInfoBean getVideo_info() {
            return video_info;
        }

        public void setVideo_info(VideoInfoBean video_info) {
            this.video_info = video_info;
        }

        public FilesBean getFiles() {
            return files;
        }

        public void setFiles(FilesBean files) {
            this.files = files;
        }

        public String getMin_definition() {
            return min_definition;
        }

        public void setMin_definition(String min_definition) {
            this.min_definition = min_definition;
        }

        public String getMax_definition() {
            return max_definition;
        }

        public void setMax_definition(String max_definition) {
            this.max_definition = max_definition;
        }

        public MvInfoBean getMv_info() {
            return mv_info;
        }

        public void setMv_info(MvInfoBean mv_info) {
            this.mv_info = mv_info;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public static class VideoInfoBean {
            /**
             * video_id : 334807047
             * mv_id : 334807046
             * provider : 11
             * sourcepath : null
             * thumbnail : http://qukufile2.qianqian.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_160
             * thumbnail2 : http://qukufile2.qianqian.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_240
             * del_status : 0
             * distribution : 0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000,0000000000
             */

            private String video_id;
            private String mv_id;
            private String provider;
            private Object sourcepath;
            private String thumbnail;
            private String thumbnail2;
            private String del_status;
            private String distribution;

            public String getVideo_id() {
                return video_id;
            }

            public void setVideo_id(String video_id) {
                this.video_id = video_id;
            }

            public String getMv_id() {
                return mv_id;
            }

            public void setMv_id(String mv_id) {
                this.mv_id = mv_id;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }

            public Object getSourcepath() {
                return sourcepath;
            }

            public void setSourcepath(Object sourcepath) {
                this.sourcepath = sourcepath;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getThumbnail2() {
                return thumbnail2;
            }

            public void setThumbnail2(String thumbnail2) {
                this.thumbnail2 = thumbnail2;
            }

            public String getDel_status() {
                return del_status;
            }

            public void setDel_status(String del_status) {
                this.del_status = del_status;
            }

            public String getDistribution() {
                return distribution;
            }

            public void setDistribution(String distribution) {
                this.distribution = distribution;
            }
        }

        public static class FilesBean {
            /**
             * 31 : {"video_file_id":"334807049","video_id":"334807047","definition":"31","file_link":"http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=8ba0f12fd0b003a59c2a543283853183&tvid=676216800","file_format":"","file_extension":"mp4","file_duration":"232","file_size":"0","source_path":"http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=8ba0f12fd0b003a59c2a543283853183&tvid=676216800"}
             */

            @SerializedName("31")
            private _$31Bean _$31;

            public _$31Bean get_$31() {
                return _$31;
            }

            public void set_$31(_$31Bean _$31) {
                this._$31 = _$31;
            }

            public static class _$31Bean {
                /**
                 * video_file_id : 334807049
                 * video_id : 334807047
                 * definition : 31
                 * file_link : http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=8ba0f12fd0b003a59c2a543283853183&tvid=676216800
                 * file_format :
                 * file_extension : mp4
                 * file_duration : 232
                 * file_size : 0
                 * source_path : http://dispatcher.video.qiyi.com/common/shareplayer.html?vid=8ba0f12fd0b003a59c2a543283853183&tvid=676216800
                 */

                private String video_file_id;
                private String video_id;
                private String definition;
                private String file_link;
                private String file_format;
                private String file_extension;
                private String file_duration;
                private String file_size;
                private String source_path;

                public String getVideo_file_id() {
                    return video_file_id;
                }

                public void setVideo_file_id(String video_file_id) {
                    this.video_file_id = video_file_id;
                }

                public String getVideo_id() {
                    return video_id;
                }

                public void setVideo_id(String video_id) {
                    this.video_id = video_id;
                }

                public String getDefinition() {
                    return definition;
                }

                public void setDefinition(String definition) {
                    this.definition = definition;
                }

                public String getFile_link() {
                    return file_link;
                }

                public void setFile_link(String file_link) {
                    this.file_link = file_link;
                }

                public String getFile_format() {
                    return file_format;
                }

                public void setFile_format(String file_format) {
                    this.file_format = file_format;
                }

                public String getFile_extension() {
                    return file_extension;
                }

                public void setFile_extension(String file_extension) {
                    this.file_extension = file_extension;
                }

                public String getFile_duration() {
                    return file_duration;
                }

                public void setFile_duration(String file_duration) {
                    this.file_duration = file_duration;
                }

                public String getFile_size() {
                    return file_size;
                }

                public void setFile_size(String file_size) {
                    this.file_size = file_size;
                }

                public String getSource_path() {
                    return source_path;
                }

                public void setSource_path(String source_path) {
                    this.source_path = source_path;
                }
            }
        }

        public static class MvInfoBean {
            /**
             * mv_id : 334807046
             * all_artist_id : 48865
             * title : Consommez (Tryo détourne Bercy 2009) (Live Video)
             * aliastitle :
             * subtitle : Tryo - Consommez (Tryo détourne Bercy 2009) (Live Video)
             * play_nums : 0
             * publishtime : 1970-01-01
             * del_status : 0
             * artist_list : [{"artist_id":"48865","ting_uid":"88009156","artist_name":"Tryo","artist_480_800":"","artist_640_1136":"","avatar_small":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_48","avatar_mini":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_20","avatar_s180":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_180","avatar_s300":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_300","avatar_s500":"http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_500","del_status":"0"}]
             * artist_id : 48865
             * thumbnail : http://musicdata.baidu.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_160
             * thumbnail3 : http://business.cdn.qianqian.com/baidumisic
             * thumbnail2 : http://musicdata.baidu.com/data2/pic/344a8dda9162de3e24b91b39ba572165/334807050/334807050.jpg@s_0,w_240
             * artist : Tryo
             * provider : 11
             */

            private String mv_id;
            private String all_artist_id;
            private String title;
            private String aliastitle;
            private String subtitle;
            private String play_nums;
            private String publishtime;
            private String del_status;
            private String artist_id;
            private String thumbnail;
            private String thumbnail3;
            private String thumbnail2;
            private String artist;
            private String provider;
            private List<ArtistListBean> artist_list;

            public String getMv_id() {
                return mv_id;
            }

            public void setMv_id(String mv_id) {
                this.mv_id = mv_id;
            }

            public String getAll_artist_id() {
                return all_artist_id;
            }

            public void setAll_artist_id(String all_artist_id) {
                this.all_artist_id = all_artist_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAliastitle() {
                return aliastitle;
            }

            public void setAliastitle(String aliastitle) {
                this.aliastitle = aliastitle;
            }

            public String getSubtitle() {
                return subtitle;
            }

            public void setSubtitle(String subtitle) {
                this.subtitle = subtitle;
            }

            public String getPlay_nums() {
                return play_nums;
            }

            public void setPlay_nums(String play_nums) {
                this.play_nums = play_nums;
            }

            public String getPublishtime() {
                return publishtime;
            }

            public void setPublishtime(String publishtime) {
                this.publishtime = publishtime;
            }

            public String getDel_status() {
                return del_status;
            }

            public void setDel_status(String del_status) {
                this.del_status = del_status;
            }

            public String getArtist_id() {
                return artist_id;
            }

            public void setArtist_id(String artist_id) {
                this.artist_id = artist_id;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getThumbnail3() {
                return thumbnail3;
            }

            public void setThumbnail3(String thumbnail3) {
                this.thumbnail3 = thumbnail3;
            }

            public String getThumbnail2() {
                return thumbnail2;
            }

            public void setThumbnail2(String thumbnail2) {
                this.thumbnail2 = thumbnail2;
            }

            public String getArtist() {
                return artist;
            }

            public void setArtist(String artist) {
                this.artist = artist;
            }

            public String getProvider() {
                return provider;
            }

            public void setProvider(String provider) {
                this.provider = provider;
            }

            public List<ArtistListBean> getArtist_list() {
                return artist_list;
            }

            public void setArtist_list(List<ArtistListBean> artist_list) {
                this.artist_list = artist_list;
            }

            public static class ArtistListBean {
                /**
                 * artist_id : 48865
                 * ting_uid : 88009156
                 * artist_name : Tryo
                 * artist_480_800 :
                 * artist_640_1136 :
                 * avatar_small : http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_48
                 * avatar_mini : http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_20
                 * avatar_s180 : http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_180
                 * avatar_s300 : http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_300
                 * avatar_s500 : http://musicdata.baidu.com/data2/music/6BCC626F1EE8896C3AF1305D667054F4/252346452/252346452.jpg@s_0,w_500
                 * del_status : 0
                 */

                private String artist_id;
                private String ting_uid;
                private String artist_name;
                private String artist_480_800;
                private String artist_640_1136;
                private String avatar_small;
                private String avatar_mini;
                private String avatar_s180;
                private String avatar_s300;
                private String avatar_s500;
                private String del_status;

                public String getArtist_id() {
                    return artist_id;
                }

                public void setArtist_id(String artist_id) {
                    this.artist_id = artist_id;
                }

                public String getTing_uid() {
                    return ting_uid;
                }

                public void setTing_uid(String ting_uid) {
                    this.ting_uid = ting_uid;
                }

                public String getArtist_name() {
                    return artist_name;
                }

                public void setArtist_name(String artist_name) {
                    this.artist_name = artist_name;
                }

                public String getArtist_480_800() {
                    return artist_480_800;
                }

                public void setArtist_480_800(String artist_480_800) {
                    this.artist_480_800 = artist_480_800;
                }

                public String getArtist_640_1136() {
                    return artist_640_1136;
                }

                public void setArtist_640_1136(String artist_640_1136) {
                    this.artist_640_1136 = artist_640_1136;
                }

                public String getAvatar_small() {
                    return avatar_small;
                }

                public void setAvatar_small(String avatar_small) {
                    this.avatar_small = avatar_small;
                }

                public String getAvatar_mini() {
                    return avatar_mini;
                }

                public void setAvatar_mini(String avatar_mini) {
                    this.avatar_mini = avatar_mini;
                }

                public String getAvatar_s180() {
                    return avatar_s180;
                }

                public void setAvatar_s180(String avatar_s180) {
                    this.avatar_s180 = avatar_s180;
                }

                public String getAvatar_s300() {
                    return avatar_s300;
                }

                public void setAvatar_s300(String avatar_s300) {
                    this.avatar_s300 = avatar_s300;
                }

                public String getAvatar_s500() {
                    return avatar_s500;
                }

                public void setAvatar_s500(String avatar_s500) {
                    this.avatar_s500 = avatar_s500;
                }

                public String getDel_status() {
                    return del_status;
                }

                public void setDel_status(String del_status) {
                    this.del_status = del_status;
                }
            }
        }
    }
}
