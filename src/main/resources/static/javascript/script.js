function buttonClicked(url) {
    window.location.href = url; // 지정된 URL로 이동
}

function toggleNumber(displayId) {
    const displayElement = document.getElementById(displayId);
    // 현재 숫자 가져오기
    const currentNumber = parseInt(displayElement.textContent);
    // 숫자 변경
    const newNumber = currentNumber === 0 ? 1 : 0;
    displayElement.textContent = newNumber; // 숫자 업데이트
}
