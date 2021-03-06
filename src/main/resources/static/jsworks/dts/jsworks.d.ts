import { JSWorksController } from './Controller/ControllerDecorator';
import { JSWorksComponent } from './Component/ComponentDecorator';
import { JSWorksPage } from './Component/PageDecorator';
import { JSWorksService } from './Service/ServiceDecorator';
import { JSWorksComponentProperty } from './Component/ComponentPropertyDecorator';
import { JSWorksCustomElement } from './CustomElements/CustomElementDecorator';
import { JSWorksComponentCollectionProperty } from './Component/ComponentCollectionPropertyDecorator';
import { EventType } from './EventManager/EventType';
import { InterceptorType } from './Interceptor/InterceptorType';
import { JSWorksInterceptor } from './Interceptor/InterceptorDecorator';
import { JSWorksModel } from './Model/Decorators/ModelDecorator';
import { JSWorksModelField } from './Model/Decorators/ModelFieldDecorator';
import { JSWorksModelPrimaryKey } from './Model/Decorators/ModelPrimaryKeyDecorator';
import { JSWorksModelCreateMethod } from './Model/Decorators/ModelCreateMethodDecorator';
import { JSWorksModelReadMethod } from './Model/Decorators/ModelReadMethodDecorator';
import { JSWorksModelUpdateMethod } from './Model/Decorators/ModelUpdateMethodDecorator';
import { JSWorksModelDeleteMethod } from './Model/Decorators/ModelDeleteMethodDecorator';
import { JSWorksModelQueryMethod } from './Model/Decorators/ModelQueryMethodDecorator';
import { HTTPMethod } from './Network/HTTPMethod';
import { EventManager } from './EventManager/EventManager';
import { ApplicationContext } from './ApplicationContext/ApplicationContext';
import { ServiceHolder } from './Service/ServiceHolder';
export declare class JSWorksLib {
    Internal: object;
    EventManager: typeof EventManager;
    EventType: typeof EventType;
    InterceptorType: typeof InterceptorType;
    HTTPMethod: {
        DELETE: HTTPMethod;
        GET: HTTPMethod;
        HEAD: HTTPMethod;
        OPTIONS: HTTPMethod;
        PATCH: HTTPMethod;
        POST: HTTPMethod;
        PUT: HTTPMethod;
    };
    Service: typeof JSWorksService;
    Controller: typeof JSWorksController;
    Interceptor: typeof JSWorksInterceptor;
    Component: typeof JSWorksComponent;
    Page: typeof JSWorksPage;
    ComponentProperty: typeof JSWorksComponentProperty;
    CustomElement: typeof JSWorksCustomElement;
    ComponentCollectionProperty: typeof JSWorksComponentCollectionProperty;
    Model: typeof JSWorksModel;
    ModelField: typeof JSWorksModelField;
    ModelPrimaryKey: typeof JSWorksModelPrimaryKey;
    ModelCreateMethod: typeof JSWorksModelCreateMethod;
    ModelReadMethod: typeof JSWorksModelReadMethod;
    ModelUpdateMethod: typeof JSWorksModelUpdateMethod;
    ModelDeleteMethod: typeof JSWorksModelDeleteMethod;
    ModelQueryMethod: typeof JSWorksModelQueryMethod;
    applicationContext: ApplicationContext;
    config: object;
    constructor();
    registerServices(): ServiceHolder;
}
