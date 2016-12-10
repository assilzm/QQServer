package qq

/**
 *
 * @author: weiwei
 * create: 2016-11-23 20:12.
 * description:
 */
enum SendMessageType {
    FRIEND(1),GROUP(2),DISCUSS(3)


    int type

    SendMessageType(int type){
        this.type=type
    }
}