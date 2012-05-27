<%@include  file="/WEB-INF/pages/commons/header.jsp"%>
<div id="content">    
    <h1><spring:message code="message.error.internal"/></h1> 
    <div class="text-holder">
        <h3>Congratulations, you broke the Internet! Are you happy now?</h3>
        <p>
            <b>We're sorry, but something is technically wrong</b> &mdash; Our most skilled code monkeys have already been notified about this problem and are doing everything they can to resolve this situation.
        </p>
        <p>
            If you can give us further information about what happened, please email to us on <b><a href='mailto:<spring:message code="message.email.registration.from"/>'><spring:message code="message.email.registration.from"/></a></b>.</p>
        <p>
            Feel free to send us questions or comments via <b><a href="/contact">Contact form</a></b>.         
        </p>
    </div>
</div>
<%@include  file="/WEB-INF/pages/commons/footer.jsp"%> 