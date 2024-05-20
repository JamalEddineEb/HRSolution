import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { IDomain } from 'app/entities/domain/domain.model';
import { IApplication } from 'app/entities/application/application.model';

export interface ICandidate {
  id: number;
  linkedinUrl?: string | null;
  fullName?: string | null;
  yearsOfExperience?: number | null;
  resume?: string | null;
  resumeContentType?: string | null;
  currentSalary?: number | null;
  desiredSalary?: number | null;
  hasContract?: boolean | null;
  hired?: boolean | null;
  rate?: number | null;
  techCV?: ITechnicalCV | null;
  domains?: IDomain[] | null;
  applications?: IApplication[] | null;
}

export type NewCandidate = Omit<ICandidate, 'id'> & { id: null };
