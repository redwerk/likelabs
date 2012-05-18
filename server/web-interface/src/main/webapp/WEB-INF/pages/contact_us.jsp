<%@include file="./header.jsp" %>
<style type="text/css">
    .error {
        color: #ff0000;
    }
    .errorblock{
        color: #000;
        background-color: #ffEEEE;
        border: 3px solid #ff0000;
        padding:8px;
        margin:16px;
    }
</style>
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
                        <td><form:errors path="name" cssClass="error"/></td>
                    </tr>
                    <tr>
                        <td><label for="email">Your email: </label></td>
                        <td><form:input path="email" /></td>
                        <td><form:errors path="email" cssClass="error"/></td>
                    </tr>
                    <tr>
                        <td><label for="summary">Summary: </label></td>
                        <td><form:input path="summary" /></td>
                        <td><form:errors path="summary" cssClass="error"/></td>
                    </tr>
                    <tr>
                        <td><label for="message">Message: </label></td>
                        <td><form:textarea path="message" rows="5" cols="40" ></form:textarea></td>
                        <td><form:errors path="message" cssClass="error"/></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center"><input type="submit" value="Send"></td>
                    </tr>
                </table>
            </form:form>
        </td>
    </tr>
</table>
<%@include file="./footer.jsp" %>