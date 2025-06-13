function viewKcisaBookDetail(btn) {
	const title = btn.getAttribute("data-title");
	const isbn = btn.getAttribute("data-isbn");
	const rmonth = btn.getAttribute("data-rmonth");
	const encodedTitle = encodeURIComponent(title);
	document.getElementById('loadingBar').style.display = 'flex';

	fetch('/api/searchKcisaBook?title=' + encodedTitle + '&isbn=' + isbn)
		.then(response => {
			if (!response.ok) throw new Error("도서 정보를 가져오지 못했습니다.");
			return response.json();
		})
		.then(book => {
			const detailHtml = `
				<img src="${book.image}" alt="도서 이미지" class="book-image">                
				<p><a href="/searchLibraryPage/${isbn}?title=${encodedTitle}" target="_blank" class="library-link">
					📚 이 도서를 소장한 도서관 찾기
				</a></p>
				<p><strong>제목:</strong> ${book.title}</p>
				<p><strong>저자:</strong> ${book.author && book.author.trim() !== "" ? book.author : book.rights}</p>
				<p><strong>출판사:</strong> ${book.publisher && book.publisher.trim() !== "" ? book.publisher : book.rights}</p>
				<p><strong>추천연월:</strong> ${rmonth}</p>
				<p><strong>책소개</strong> <div class="book-description">${book.description}</div></p>
			`;
			document.getElementById('bookDetailArea').innerHTML = detailHtml;
			document.getElementById('bookModal').style.display = 'block';
		})
		.catch(error => {
			alert(error.message);
		})
		.finally(() => {
			document.getElementById('loadingBar').style.display = 'none';
		});
}

function closeModal() {
    document.getElementById('bookModal').style.display = 'none';
}