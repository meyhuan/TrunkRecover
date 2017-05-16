package com.meyhuan.trunkrecover.rxbus;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public interface RxInterface {

    class RxEvent implements RxInterface {
    }



    class SingleEvent<T> extends RxEvent{
        public T param1;

        public SingleEvent(T param1){
            this.param1 = param1;
        }
    }

    class TwinEvent<T, K> extends RxEvent{
        public T param1;
        public K param2;

        public TwinEvent(T param1, K param2){
            this.param1 = param1;
            this.param2 = param2;
        }
    }

    class TrisEvent<T, K, V> extends RxEvent{
        public T param1;
        public K param2;
        public V param3;

        public TrisEvent(T param1, K param2, V param3){
            this.param1 = param1;
            this.param2 = param2;
            this.param3 = param3;
        }
    }
}
