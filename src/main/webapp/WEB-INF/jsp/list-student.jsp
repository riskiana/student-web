    <html>
    <body>
        <h1>STUDENT WEB</h1>
        <p>
           Daftar Mahasiswa UBL:${riski}</h1>

        <ul>
            <c:forEach var="student" items="${students}">
                NIM: <li>${student.nim}</li>
             </c:forEach>
        </ul>
        </p>
    </body>
    </html>