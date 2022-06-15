
export function getFormatTime(time) {
  if (!time) {
    return '';
  }
  var oDate = new Date();
  var newHaoMiao1 = oDate.getTime(); //当前时间,含有时分秒
  oDate.setHours(0);
  oDate.setMinutes(0);
  oDate.setSeconds(0);
  oDate.setMilliseconds(0);
  var newHaoMiao2 = oDate.getTime(); //当前时间,不含有时分秒
  var newTime = time.replace(new RegExp("-", "gm"), "/");
  var arrTime = time.substring(0, 11).replace(new RegExp("-", "gm"), "/");     //截取时间，不含有时分秒
  var showTime = time.substring(0, 11);
  var oldHaoMiao1 = new Date(newTime).getTime();   //含有时分秒的转化成毫秒
  var oldHaoMiao2 = new Date(arrTime).getTime();     //不含有时分秒的转化成毫秒
  var d1 = (newHaoMiao1 - oldHaoMiao1) / 1000;
  var d2 = (newHaoMiao2 - oldHaoMiao2) / 1000;
  var d_result = '';
  if (d2 > 0) {   //是几天前
    var d_days = parseInt(d2 / 86400);
    if (d_days === 1) {
      d_result = "昨天";
    } else if (d_days >= 2) {
      d_result = `${d_days}天前`;
    }
  } else {    //是今天
    var d_hours = parseInt(d1 / 3600);
    var d_minutes = parseInt(d1 / 60);
    if (d_hours > 0) {
      d_result = d_hours + "小时前";
    } else if (d_hours <= 0 && d_minutes > 0) {
      d_result = d_minutes + "分钟前";
    } else {
      d_result = "刚刚";
    }
  }
  return d_result;
}


