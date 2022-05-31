<template>
  <div class="Page Confirm">
    <div class="Title">
      <h1 class="fl f18">
        订单确认
        <span class="red f20"
          ><strong id="AllPrice">{{ leftTime }}</strong></span
        >
      </h1>
      <img src="~/assets/img/cart_setp2.png" class="fr" />
      <div class="clear"></div>
    </div>
    <form name="flowForm" id="flowForm" method="post" :action="formUrl">
      <table class="GoodList">
        <tbody>
          <tr>
            <th class="name">商品</th>
            <th class="price">原价</th>
            <th class="priceNew">价格</th>
          </tr>
        </tbody>
        <tbody>
          <!-- <tr>
<td colspan="3" class="Title red f18 fb"><p>限时折扣</p></td>
</tr> -->
          <tr>
            <td colspan="3" class="teacher">讲师：{{ order.teacherName }}</td>
          </tr>
          <tr class="good">
            <td class="name First">
              <a
                target="_blank"
                :href="'/course/' + order.courseId"
              >
                <img :src="order.courseCover"
              /></a>
              <div class="goodInfo">
                <input type="hidden" class="ids ids_14502" value="14502" />
                <a
                  target="_blank"
                  :href="'/course/' + order.courseId"
                  >{{ order.courseTitle }}</a
                >
              </div>
            </td>
            <td class="price">
              <p>
                ￥<strong>{{ order.totalFee }}</strong>
              </p>
              <!-- <span class="discName red">限时8折</span> -->
            </td>
            <td class="red priceNew Last">
              ￥<strong>{{ order.totalFee }}</strong>
            </td>
          </tr>
          <tr>
            <td class="Billing tr" colspan="3">
              <div class="tr">
                <p>
                  共 <strong class="red">1</strong> 件商品，合计<span
                    class="red f20"
                    >￥<strong>{{ order.totalFee }}</strong></span
                  >
                </p>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="Finish">
        <div class="fr" id="AgreeDiv">
          <label for="Agree"
            ><p class="on">
              <input type="checkbox" checked="checked" />我已阅读并同意<a
                href="javascript:"
                target="_blank"
                >《Nov课堂购买 协议》</a
              >
            </p></label
          >
        </div>
        <div class="clear"></div>
        <div class="Main fl">
          <div class="fl">
            <a :href="'/course/' + order.courseId">返回课程详情页</a>
          </div>
          <div class="fr">
            <p>
              共 <strong class="red">1</strong> 件商品，合计<span
                class="red f20"
                >￥<strong id="AllPrice">{{ order.totalFee }}</strong></span
              >
            </p>
          </div>
        </div>
        <input name="score" value="0" type="hidden" id="usedScore" />

        <button
          class="fr redb"
          type="button"
          id="submitPay"
          @click="toPay($event)"
        >
          去支 付
        </button>
        <a id="tagOpenWin" target="_blank"></a>
        <div class="clear"></div>
      </div>
    </form>
  </div>
</template>

<script>
import { createOrder, getOrderById } from "@/api/order";

export default {
  data() {
    return {
      token: null,
      order: {},
      leftTime: "",
      formUrl: "http://159.75.234.20:8000/order/alipay/web?id=",
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      let id = this.$route.params.id;
      let intervalId = "st";
      getOrderById(id).then((resp) => {
        if (resp.code === 200) {
          this.order = resp.data;
          this.formUrl += this.order.id;
          let start = new Date(this.order.createTime);
          let end = new Date(start.setMinutes(start.getMinutes() + 30));
          intervalId = setInterval(() => {
            let now = new Date();
            let left = end.getTime() - now.getTime();
            //计算出相差天数
            let days = Math.floor(left / (24 * 3600 * 1000));
            //计算出小时数
            let leave1 = left % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
            let hours = Math.floor(leave1 / (3600 * 1000));
            //计算相差分钟数
            let leave2 = leave1 % (3600 * 1000); //计算小时数后剩余的毫秒数
            let minutes = Math.floor(leave2 / (60 * 1000));
            //计算相差秒数
            let leave3 = leave2 % (60 * 1000); //计算分钟数后剩余的毫秒数
            let seconds = Math.round(leave3 / 1000);
            this.leftTime = `${minutes}分${seconds}秒内未完成支付将取消订单`;
            if (minutes <= 0 && seconds <= 0) {
              clearInterval(intervalId);
              this.$router.go(-1);
            }
          }, 1000);
        }
      });
    },
    toPay() {
      var obj = document.getElementById("tagOpenWin");
      obj.href = this.formUrl;
      obj.click();
       this.$confirm("是否已经成功支付?", "订单提示", {
          confirmButtonText: "我已经完成支付",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(()=>{
              this.$router.go(-1);
          })
    },
  },
};
</script>

