<%@include file="./header.jsp" %>
<table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
    <tr>
        <td class="title">Contact Us</td>
    </tr>
    <tr>
        <td class="body">
            <p>
                Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            </p>
            <form:form method="POST" commandName="message">
                <table cellpadding="0" cellspacing="0" summary="" class="contact_form">
                    <tr>
                        <td><label for="name">Your name: </label></td>
                        <td><form:input path="name" /></td>
                        <td><form:errors path="name" cssClass="errorblock"/></td>
                    </tr>
                    <tr>
                        <td><label for="email">Your email: </label></td>
                        <td><form:input path="email" /></td>
                        <td><form:errors path="email" cssClass="errorblock"/></td>
                    </tr>
                    <tr>
                        <td><label for="summary">Summary: </label></td>
                        <td><form:input path="summary" /></td>
                        <td><form:errors path="summary" cssClass="errorblock"/></td>
                    </tr>
                    <tr>
                        <td><label for="message">Message: </label></td>
                        <td><form:textarea path="message" rows="5" cols="80" ></form:textarea></td>
                        <td><form:errors path="message" cssClass="errorblock"/></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td style="text-align: center"><button type="submit" class="btn btn_success save">Send</button></td>
                    </tr>
                </table>
            </form:form>
        </td>
    </tr>
</table>
<%@include file="./footer.jsp" %>