function filterByTag(tag) {
    // 현재 URL의 쿼리 파라미터에서 query 값을 추출
    const currentUrlParams = new URLSearchParams(window.location.search);
    const query = currentUrlParams.get('query'); // 기존 query 값 가져오기

    // 태그가 존재하면 해당 태그로 이동하고, query 값도 함께 전달
    if (tag) {
        const url = query ? `/search/${encodeURIComponent(tag)}?query=${encodeURIComponent(query)}` : `/search/${encodeURIComponent(tag)}`;
        window.location.href = url; // 태그와 query 값을 포함한 URL로 이동
    } else {
        // 태그 선택 해제 시 query 값만 포함하여 검색 페이지로 이동
        window.location.href = query ? `/search?query=${encodeURIComponent(query)}` : `/search`;
    }
}
