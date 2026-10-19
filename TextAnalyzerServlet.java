import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Tomcat 10+ қолданса: javax -> jakarta деп ауыстырыңыз
@WebServlet("/analyze")
public class TextAnalyzerServlet extends HttpServlet {

    // Форманы көрсету
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        printHeader(out);
        printForm(out, "");
        printFooter(out);
    }

    // Мәтінді талдау
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // getParameter-ден бұрын!
        response.setContentType("text/html;charset=UTF-8");

        String text = request.getParameter("text");
        PrintWriter out = response.getWriter();

        printHeader(out);
        printForm(out, text == null ? "" : text);

        if (text == null || text.trim().isEmpty()) {
            out.println("<p style='color:red'><b>Мәтін енгізілмеді</b></p>");
        } else {
            int charCount = text.length();                          // бос орындарымен
            int charNoSpaces = text.replaceAll("\\s", "").length(); // бос орынсыз
            int wordCount = text.trim().split("\\s+").length;

            out.println("<h3>Талдау нәтижесі</h3>");
            out.println("<p>Таңбалар саны (бос орынмен): <b>" + charCount + "</b></p>");
            out.println("<p>Таңбалар саны (бос орынсыз): <b>" + charNoSpaces + "</b></p>");
            out.println("<p>Сөздер саны: <b>" + wordCount + "</b></p>");
            out.println("<p>Енгізілген мәтін: <i>" + escapeHtml(text) + "</i></p>"); // XSS-тен қорғау
        }
        printFooter(out);
    }

    private void printHeader(PrintWriter out) {
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        out.println("<title>Мәтін талдау</title></head><body>");
        out.println("<h2>Мәтін талдау</h2>");
    }

    private void printForm(PrintWriter out, String value) {
        out.println("<form action='analyze' method='post'>");
        out.println("<textarea name='text' rows='6' cols='50' "
                + "placeholder='Мәтінді осында енгізіңіз'>"
                + escapeHtml(value) + "</textarea><br><br>");
        out.println("<button type='submit'>Талдау</button>");
        out.println("</form>");
    }

    private void printFooter(PrintWriter out) {
        out.println("</body></html>");
    }

    // HTML арнайы таңбаларын экрандау
    private String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
