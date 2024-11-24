function filterByTag(tag) {
    if (tag) {
        // 태그 선택 시 해당 태그로 요청
        window.location.href = `/search/${encodeURIComponent(tag)}`;
    } else {
        // 태그 선택 해제 시 검색 페이지로 이동
        window.location.href = `/search`;
    }
}
