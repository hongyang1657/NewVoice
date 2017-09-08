package fitme.ai.bean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by blw on 2016/9/12.
 */
public class MessageGet {
    private String status;
    private Messages[] messages;

    public MessageGet() {
    }

    public MessageGet(String status, Messages[] messages) {
        this.status = status;
        this.messages = messages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MessageGet{" +
                "status='" + status + '\'' +
                ", messages=" + Arrays.toString(messages) +
                ",messages[0]="+messages[0].toString()+
                '}';
    }

    public Messages[] getMessages() {
        return messages;
    }

    public void setMessages(Messages[] messages) {
        this.messages = messages;
    }

    public class Messages {
        private String message_id;
        private long time;
        private String message_type;
        private MessageBody message_body;

        public Messages() {
        }

        public Messages(String message_id, long time, String message_type, MessageBody message_body) {
            this.message_id = message_id;
            this.time = time;
            this.message_type = message_type;
            this.message_body = message_body;
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getMessage_type() {
            return message_type;
        }

        public void setMessage_type(String message_type) {
            this.message_type = message_type;
        }

        public MessageBody getMessage_body() {
            return message_body;
        }

        public void setMessage_body(MessageBody message_body) {
            this.message_body = message_body;
        }

        @Override
        public String toString() {
            return "message_id="+message_id+",time="+time+",message_type="+message_type+",message_body="+message_body.toString();
        }
    }

    /**
     * recommendations推荐类型
     */
    public class Recommendation{
        private String task_title;
        private String task_type;
        private Map<String,Object> task_body;

        public Recommendation(String task_title, String task_type, Map<String, Object> task_body) {
            this.task_title = task_title;
            this.task_type = task_type;
            this.task_body = task_body;
        }

        public String getTask_title() {
            return task_title;
        }

        public void setTask_title(String task_title) {
            this.task_title = task_title;
        }

        public String getTask_type() {
            return task_type;
        }

        public void setTask_type(String task_type) {
            this.task_type = task_type;
        }

        public Map<String, Object> getTask_body() {
            return task_body;
        }

        public void setTask_body(Map<String, Object> task_body) {
            this.task_body = task_body;
        }
    }

    public class MessageBody {
        private String parent_task_id;
        private String task_result_id;
        private String task_id;
        private String message_id;
        private long task_result_time;
        private String task_type;
        private TaskResultBody task_result_body;
        private String task_result_speech_text;
        private Recommendation[] recommendations;
        private String content;
        private String[] contents;



        public String[] getContents() {
            return contents;
        }

        public void setContents(String[] contents) {
            this.contents = contents;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTask_result_speech_text() {
            return task_result_speech_text;
        }

        public MessageBody() {

        }

        public String getParent_task_id() {
            return parent_task_id;
        }

        public void setParent_task_id(String parent_task_id) {
            this.parent_task_id = parent_task_id;
        }

        public MessageBody(String parent_task_id, String task_result_id, String task_id, String message_id, long task_result_time, String task_type, TaskResultBody task_result_body, String task_result_speech_text, Recommendation[] recommendations, String content) {
            this.parent_task_id = parent_task_id;
            this.task_result_id = task_result_id;
            this.task_id = task_id;
            this.message_id = message_id;
            this.task_result_time = task_result_time;
            this.task_type = task_type;
            this.task_result_body = task_result_body;
            this.task_result_speech_text = task_result_speech_text;
            this.recommendations = recommendations;
            this.content = content;
        }

        public void setTask_result_speech_text(String task_result_speech_text) {
            this.task_result_speech_text = task_result_speech_text;
        }

        public Recommendation[] getRecommendations() {
            return recommendations;
        }

        public void setRecommendations(Recommendation[] recommendations) {
            this.recommendations = recommendations;
        }

        public String getTask_result_id() {
            return task_result_id;
        }

        public void setTask_result_id(String task_result_id) {
            this.task_result_id = task_result_id;
        }

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public long getTask_result_time() {
            return task_result_time;
        }

        public void setTask_result_time(long task_result_time) {
            this.task_result_time = task_result_time;
        }

        public String getTask_type() {
            return task_type;
        }

        public void setTask_type(String task_type) {
            this.task_type = task_type;
        }

        public TaskResultBody getTask_result_body() {
            return task_result_body;
        }

        public void setTask_result_body(TaskResultBody task_result_body) {
            this.task_result_body = task_result_body;
        }


        public class TaskResultBody{
            private String code;
            private String msg;
            private String task_type;
            private String type;
            private String device;
            private String command;
            private String film_name;

            private List<Music> musics;
            private List<Devices> devices;
            private String videoUrl;

            private String nickname;
            private int device_type;
            private String did;
            private String command_code;

            public String getFilm_name() {
                return film_name;
            }

            public void setFilm_name(String film_name) {
                this.film_name = film_name;
            }

            public String getVideoUrl() {
                return videoUrl;
            }

            public void setVideoUrl(String videoUrl) {
                this.videoUrl = videoUrl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDevice() {
                return device;
            }

            public void setDevice(String device) {
                this.device = device;
            }

            public String getCommand() {
                return command;
            }

            public void setCommand(String command) {
                this.command = command;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getDevice_type() {
                return device_type;
            }

            public void setDevice_type(int device_type) {
                this.device_type = device_type;
            }

            public String getDid() {
                return did;
            }

            public void setDid(String did) {
                this.did = did;
            }

            public String getCommand_code() {
                return command_code;
            }

            public void setCommand_code(String command_code) {
                this.command_code = command_code;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getTask_type() {
                return task_type;
            }

            public void setTask_type(String task_type) {
                this.task_type = task_type;
            }

            public List<Music> getMusics() {
                return musics;
            }

            public void setMusics(List<Music> musics) {
                this.musics = musics;
            }

            public List<Devices> getDevices() {
                return devices;
            }

            public void setDevices(List<Devices> devices) {
                this.devices = devices;
            }

            public class Devices{
                private String task_type;
                private String command;
                private String command_code;
                private String nickname;
                private String did;
                private String category;
                private String device_name;
                private String device_type;

                public String getTask_type() {
                    return task_type;
                }

                public void setTask_type(String task_type) {
                    this.task_type = task_type;
                }

                public String getCommand() {
                    return command;
                }

                public void setCommand(String command) {
                    this.command = command;
                }

                public String getCommand_code() {
                    return command_code;
                }

                public void setCommand_code(String command_code) {
                    this.command_code = command_code;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getDid() {
                    return did;
                }

                public void setDid(String did) {
                    this.did = did;
                }

                public String getCategory() {
                    return category;
                }

                public void setCategory(String category) {
                    this.category = category;
                }

                public String getDevice_name() {
                    return device_name;
                }

                public void setDevice_name(String device_name) {
                    this.device_name = device_name;
                }

                public String getDevice_type() {
                    return device_type;
                }

                public void setDevice_type(String device_type) {
                    this.device_type = device_type;
                }
            }
        }

        @Override
        public String toString() {
            return "MessageBody{" +
                    "task_result_id='" + task_result_id + '\'' +
                    ", task_id='" + task_id + '\'' +
                    ", message_id='" + message_id + '\'' +
                    ", task_result_time=" + task_result_time +
                    ", task_type='" + task_type + '\'' +
                    ", task_result_body=" + task_result_body +
                    '}';
        }
    }

}
