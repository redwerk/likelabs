#import "LoginService.h"
#import "URLUtil.h"
#import "XMLWriter.h"
#import "LoginParser.h"

@interface LoginService()

- (NSString*) getXmlCredentialsFromLogin: (NSString*) login andPassword:(NSString*)password;

@end

@implementation LoginService

static NSUInteger const STATUS_LOGIN_OK = 200;
static NSUInteger const STATUS_LOGOUT_OK = 204;

@synthesize settingsService = _settingsService;
@synthesize dao = _dao;

- (id) init {
    if (self = [super init]) {
        _dao = [[SettingsDao alloc] init];
        _settingsService = [[SettingsService alloc] init];
    }
    return self;
}

- (BOOL)checkLogin:(NSString*)login andPassword:(NSString*)password error:(NSError**) error {
    ASIHTTPRequest* request = [ASIHTTPRequest requestWithURL:[URLUtil getLoginUrl]];
    [request addRequestHeader:CONTENT_TYPE_KEY value:CONTENT_TYPE_VALUE];
    [request appendPostData:[[self getXmlCredentialsFromLogin:login andPassword:password] dataUsingEncoding:NSUTF8StringEncoding]];
    [request startSynchronous];
    
    if (request.error) {
        if (error) 
            *error = request.error;
        NSLog(@"Login error: %@", request.error);
    } else {
        if(request.responseStatusCode == STATUS_LOGIN_OK) {
            NSLog(@"Response string: %@", request.responseString);            
            
            NSXMLParser *parser = [[NSXMLParser alloc] initWithData:request.responseData];
            LoginParser *loginParser = [[LoginParser alloc] init];
            parser.delegate = loginParser;
            [parser parse];
            [parser release];
            
            self.dao.tabletId = loginParser.tabletId;
            self.dao.apiKey = loginParser.apiKey;
            
            [loginParser release];
            
            [self.settingsService getSettings];            
            
            return true;
        }
    }
    return false;
}

- (BOOL)checkLogout:(NSString*)login andPassword:(NSString*)password error:(NSError**) error {
    ASIHTTPRequest* request = [ASIHTTPRequest requestWithURL:[URLUtil getLogoutUrlForTablet:self.dao.tabletId]];
    [request addRequestHeader:CONTENT_TYPE_KEY value:CONTENT_TYPE_VALUE];
    [request addRequestHeader:TABLET_API_KEY value:self.dao.apiKey];
    [request appendPostData:[[self getXmlCredentialsFromLogin:login andPassword:password] dataUsingEncoding:NSUTF8StringEncoding]];
    [request startSynchronous];
    if (request.error) {
        if (error)
            *error = request.error;
        NSLog(@"Logout error: %@", request.error);
    } else {
        if (request.responseStatusCode == STATUS_LOGOUT_OK) {
            NSLog(@"Response string: %@", request.responseString);            
            return true;
        }
    }
    return false;
}

- (NSString *)getXmlCredentialsFromLogin:(NSString *)login andPassword:(NSString *)password {
    XMLWriter *writer = [[XMLWriter alloc] init];
    [writer writeStartElement:@"credentials"];
    
    [writer writeStartElement:@"login"];
    [writer writeCharacters:login];
    [writer writeEndElement];//login
    
    [writer writeStartElement:@"password"];
    [writer writeCharacters:password];
    [writer writeEndElement];//password
    
    [writer writeEndElement];//credentials
    NSString* xml = [NSString stringWithString:[writer toString]];
    [writer release];
    NSLog(@"xml: %@", xml);
    return xml;
}

- (void)dealloc {
    self.settingsService = nil;
    self.dao = nil;
    [_dao release];
    [_settingsService release];
    [super dealloc];
}

@end
