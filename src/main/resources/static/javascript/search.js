const express = require('express');
const app = express();
const port = 3000;

app.get('/search', (req, res) => {
    const query = req.query.q; // 사용자가 검색한 내용을 받아옴

    // HTML 페이지 생성, 동적으로 검색어를 <title> 태그에 삽입
    res.send(`
        <!DOCTYPE html>
        <html lang="ko">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${query} - 검색 결과</title>
        </head>
        <body>
            <h1>${query}에 대한 검색 결과</h1>
            <!-- 검색 결과 리스트 -->
        </body>
        </html>
    `);
});

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
