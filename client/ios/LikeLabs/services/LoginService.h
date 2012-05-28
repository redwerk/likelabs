#import <Foundation/Foundation.h>
#import "ASIHTTPRequest.h"
#import "SettingsDao.h"
#import "SettingsService.h"

@interface LoginService : NSObject

@property (nonatomic, retain) SettingsDao* dao;
@property (nonatomic,retain) SettingsService* settingsService;

- (BOOL)checkLogin:(NSString*)code andPassword:(NSString*)password;
- (BOOL)checkLogout:(NSString *)code andPassword:(NSString *)password;

@end
