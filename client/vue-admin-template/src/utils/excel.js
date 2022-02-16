


export function exportExcel(resp) {
  const blob = new Blob([resp.data], {
    type: "application/vnd.ms-excel; charset=utf-8",
  });
  const a = document.createElement("a");
  let href = window.URL.createObjectURL(blob);
  a.href = href;
  let fileName = resp.headers["content-disposition"]
    .split(";")[1]
    .split("=")[1]
    .split(".")[0];
  a.download = decodeURIComponent(fileName);
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  window.URL.revokeObjectURL(href);
}


