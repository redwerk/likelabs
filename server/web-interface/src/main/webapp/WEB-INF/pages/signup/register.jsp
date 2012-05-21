<%@include  file="/WEB-INF/pages/header.jsp"%>
    <table cellpadding="0" cellspacing="0" style="height: 100%;" summary="" class="content_block">
        <tr>
            <td class="title">Sign Up for Like Labs - step two</td>
        </tr>
        <tr>
            <td class="body">
                <form action="/signup/end" method="POST">
                    <div>
                        <div class="field">
                            Activation code was sent to your phone. Please enter received code here:
                        </div>
                        <div class="field errorblock">  ${error}</div>
                        <div class="field">
                            <input name="password" type="text" style="width: 180px;"/>
                        </div>
                        <div class="field">
                            <button type="submit" class="btn btn_success save">Register</button>
                        </div>
                    </div>
                </form>
            </td>
        </tr>
    </table>
<%@include  file="/WEB-INF/pages/footer.jsp"%>