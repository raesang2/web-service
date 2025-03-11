// URL 복사 기능
function copyUrl() {
    const url = window.location.href;  // 현재 페이지의 URL을 가져옵니다.
    navigator.clipboard.writeText(url).then(function() {
    }).catch(function(err) {
        alert("URL 복사에 실패했습니다. 오류: " + err);
    });
}
