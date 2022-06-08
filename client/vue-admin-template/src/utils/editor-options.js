import Quill from "quill";
import hljs from "highlight.js";
import "highlight.js/styles/androidstudio.css";

const toolbarOptions = [
  ["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线 -----['bold', 'italic', 'underline', 'strike']
  ["blockquote", "code-block"], // 引用  代码块-----['blockquote', 'code-block']
  [{ list: "ordered" }, { list: "bullet" }], // 有序、无序列表-----[{ list: 'ordered' }, { list: 'bullet' }]
  [{ header: 1 }, { header: 2 }],
  [{ script: "sub" }, { script: "super" }], // 上标/下标-----[{ script: 'sub' }, { script: 'super' }]
  [{ indent: "-1" }, { indent: "+1" }],
  [{ size: ["small", false, "large", "huge"] }], // 配置字号
  [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题-----[{ header: [1, 2, 3, 4, 5, 6, false] }]
  [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色-----[{ color: [] }, { background: [] }]
  [{ font: [] }], //显示字体选择
  [{ align: [] }], // 对齐方式-----
  ["clean"], // 清除文本格式-----
  ["link", "image", "video"], // 链接、图片、视频-----
  [{ lineheight: ["initial", "1", "1.5", "1.75", "2", "3", "4", "5"] }],
];

let Parchment = Quill.import("parchment");
console.log(Parchment);
class lineHeightAttributor extends Parchment.Attributor.Style {}
const lineHeightStyle = new lineHeightAttributor("lineHeight", "line-height", {
  scope: Parchment.Scope.INLINE,
  whitelist: ["initial", '1', '1.5', '1.75', '2', '3', '4', '5' ]
});
 //编辑器相关
 let editorOptions = {
  placeholder: "请在这里输入发布的内容",
  modules: {
    toolbar: {
      container: toolbarOptions, //工具栏
      handlers: {
        lineheight: function (value) {
          if (value) {
            this.quill.format("lineHeight", value);
          } else {
            console.log(value);
          }
        },
      },
    },
    syntax: {
      highlight: (text) => {
        return hljs.highlightAuto(text).value; // 这里就是代码高亮需要配置的地方
      },
    },
  },
}
Quill.register({ "formats/lineHeight": lineHeightStyle }, true);

export {lineHeightStyle,editorOptions}

