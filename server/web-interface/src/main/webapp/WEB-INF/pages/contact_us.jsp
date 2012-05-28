<%@include file="/WEB-INF/pages/commons/header.jsp" %>
<div id="content">
    <h1>Contact Us</h1>
    <div class="text-holder">
        <p>You can always call us on our Global Advisory Line +xx (xxx) xxx-xxxx to speak to one of our operatives direct. 
        </p>
        <p>Or why not fill in the brief Contact Form below outlining your requirements and we'll get back to you with the most up to date information on Likelabs.
        </p>
        <form:form method="POST" commandName="message" class="contact-form">
            <div class="field-holder">
                <form:errors path="name" cssClass="errorblock" cssStyle="padding-left: 160px;"/>
                <label for="name">Your name: </label>
                <form:input path="name" />
            </div>
            <div class="field-holder">
                <form:errors path="email" cssClass="errorblock" cssStyle="padding-left: 160px;"/>
                <label for="email">Your email: </label>
                <form:input path="email" />
            </div>
            <div class="field-holder">
                <form:errors path="summary" cssClass="errorblock" cssStyle="padding-left: 160px;"/>
                <label for="summary">Summary: </label>
                <form:input path="summary" />
            </div>
            <div class="field-holder">
                <form:errors path="message" cssClass="errorblock" cssStyle="padding-left: 160px;"/>
                <label for="message">Message: </label>
                <form:textarea path="message" rows="5" cols="80" style="width" ></form:textarea>
            </div>
            <div class="field-holder">
                <button type="submit" class="btn btn-success save">Send</button>
            </div>
        </form:form>
    </div>
    <div class="clear"></div>
</div>
<%@include file="/WEB-INF/pages/commons/footer.jsp" %>