document.addEventListener('DOMContentLoaded', function() {
    const likeForm = document.getElementById('likeForm');
    if (likeForm) {
        likeForm.addEventListener('submit', function(event) {
            event.preventDefault(); // 폼 제출을 막고, 비동기 요청을 처리함.
            const shopId = new FormData(likeForm).get('shopId');
            toggleLike(shopId);
        });
    }
});

function toggleLike(shopId) {
    const isLiked = document.getElementById(`likeButton-${shopId}`).classList.contains('liked');
    const url = isLiked ? '/unlike' : '/like'; 
    const messageBox = document.getElementById('likeMessage');
    
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            shopId: shopId
        })
    })
    .then(response => response.text())  // JSON이 아닌 단순 텍스트로 처리
    .then(message => {
        messageBox.textContent = message;
        messageBox.style.display = 'block';

        if (url === '/like' && message === "좋아요 되었습니다.") {
            document.getElementById(`likeButton-${shopId}`).classList.add('liked');
            updateLikeCount(shopId, 1); // 좋아요 수 증가
        } else if (url === '/unlike' && message === "좋아요가 취소되었습니다.") {
            document.getElementById(`likeButton-${shopId}`).classList.remove('liked');
            updateLikeCount(shopId, -1); // 좋아요 수 감소
        }
    })
    .catch(error => {
        console.error('Error:', error);
        messageBox.textContent = '오류가 발생했습니다.';
        messageBox.style.display = 'block';
    });
}
